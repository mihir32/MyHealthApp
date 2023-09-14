package com.example.myhealthapp.add;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhealthapp.R;
import com.example.myhealthapp.network.model.Food;
import com.google.android.material.button.MaterialButton;

import org.tensorflow.lite.support.audio.TensorAudio;
import org.tensorflow.lite.task.audio.classifier.AudioClassifier;
import org.tensorflow.lite.task.audio.classifier.Classifications;

import java.io.IOException;
import java.util.List;

public class VoiceFragment extends Fragment {
    AudioClassifier ac;
    AudioRecord ar;
    TensorAudio tensor;
    Boolean k;
    TextView detected;
    MaterialButton correct, wrong;
    String data, type, date;
    Context cntxt;

    public VoiceFragment(Context cntxt, String type, String date) {
        this.type = type;
        this.date = date;
        this.cntxt = cntxt;
        k = false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_voice, container, false);
        detected = myView.findViewById(R.id.audio);
        correct = myView.findViewById(R.id.correct);
        wrong = myView.findViewById(R.id.wrong);

        if (ContextCompat.checkSelfPermission(cntxt, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            AddViewModel adm = new ViewModelProvider(requireActivity()).get(AddViewModel.class);

            try {
                ac = AudioClassifier.createFromFile(requireContext(), "soundclassifier_with_metadata.tflite");
            } catch (IOException e) {
                e.printStackTrace();
            }
            ar = ac.createAudioRecord();
            tensor = ac.createInputTensorAudio();

            MaterialButton b = myView.findViewById(R.id.record_voice);
            b.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        startRecording();
                        k = true;
                        b.setIcon(getResources().getDrawable(R.drawable.stop));
                    } else {
                        stopRecording();
                        k = false;
                        b.setIcon(getResources().getDrawable(R.drawable.mic));
                    }
                    return false;
                }
            });

//            b.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (k) {
//                        stopRecording();
//                        k = false;
//                        b.setIcon(getResources().getDrawable(R.drawable.mic));
//                    } else {
//                        startRecording();
//                        k = true;
//                        b.setIcon(getResources().getDrawable(R.drawable.stop));
//                    }
//                }
//            });

            wrong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data = null;
                    detected.setVisibility(TextView.INVISIBLE);
                    correct.setVisibility(MaterialButton.INVISIBLE);
                    wrong.setVisibility(MaterialButton.INVISIBLE);
                }
            });

            correct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adm.getSearched(data).observe(getViewLifecycleOwner(), data -> {
                        if (data == null) {
                            Toast.makeText(requireContext(), "Cannot find the food item in our Database", Toast.LENGTH_LONG).show();
                        } else {
                            b.setVisibility(MaterialButton.INVISIBLE);
                            goToAdd(data, type, date);
                        }
                    });
                }
            });
        } else {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 100);
        }

        return myView;
    }

    public void startRecording() {
        ar.startRecording();
    }

    public void stopRecording() {
        ar.stop();
        tensor.load(ar);
        List<Classifications> output = ac.classify(tensor);
        Log.d("IMAD", String.valueOf(output.get(0).getCategories().get(0).getIndex()));
        if (output.get(0).getCategories().get(0).getIndex() == 1) {
            Toast.makeText(requireContext(), "Sorry, unable to recognize what you're saying please speak loudly.", Toast.LENGTH_SHORT).show();
        } else {
            data = output.get(0).getCategories().get(0).getLabel().substring(2);
            detected.setVisibility(TextView.VISIBLE);
            correct.setVisibility(MaterialButton.VISIBLE);
            wrong.setVisibility(MaterialButton.VISIBLE);
            detected.setText(data);
        }
    }

    public void goToAdd(Food data, String type, String date) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.voiceFrag, new AddFoodFragment(data, type, date))
                .addToBackStack("voiceF")
                .commit();
    }
}