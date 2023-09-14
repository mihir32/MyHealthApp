package com.example.myhealthapp.add;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.example.myhealthapp.R;
import com.example.myhealthapp.ml.Model;
import com.example.myhealthapp.network.model.Food;


public class CameraFragment extends Fragment {
    TextView result, confidence;
    ImageView imageView;
    Button picture;
    int imageSize = 224;
    Context cntxt;
    AddViewModel adm;
    FrameLayout fm;
    String type;
    String date;

    public CameraFragment(Context cntxt, String type, String date) {
        // Required empty public constructor
        this.cntxt = cntxt;
        this.type = type;
        this.date = date;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_camera, container, false);
        result = v.findViewById(R.id.result);
        confidence = v.findViewById(R.id.confidence);
        imageView = v.findViewById(R.id.imageView);
        picture = v.findViewById(R.id.button);
        fm = v.findViewById(R.id.takePic);

        adm = new ViewModelProvider(requireActivity()).get(AddViewModel.class);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(cntxt, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        return v;
    }

    public void classifyImage(Bitmap image) {
        try {
            Model model = Model.newInstance(requireContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes = {"Chapati", "Kadhai Paneer", "Dal Fry", "Fried Rice", "Bhindi fry", "Jeera Aloo", "Steamed Rice"};
            String[] calories = {"297", "87", "129.98", "172.84", "129.53", "101.57", "127.06"};
            result.setText(classes[maxPos]);
            confidence.setText(calories[maxPos]);

            model.close();

            adm.getSearched(classes[maxPos]).observe(getViewLifecycleOwner(), data -> {
                if (data == null) {
                    Toast.makeText(requireContext(), "Cannot find the food item in our Database", Toast.LENGTH_LONG).show();
                } else {
                    goToAdd(data, type, date);
                }
            });
        } catch (IOException e) {
            // TODO Handle the exception
            Log.d("IMAD", e.getMessage());
        }
    }

    public void goToAdd(Food data, String type, String date) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.takePic, new AddFoodFragment(data, type, date))
                .addToBackStack("cameraF")
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
//            Bitmap image = null;
            try {
//                image = MediaStore.Images.Media.getBitmap(cntxt.getContentResolver(), data.getData());
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);
                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}