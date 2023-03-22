package com.example.employeesassignment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import com.example.employeesassignment.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    MusicService musicService;
    boolean isBound = false;
    private final ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            musicService = ((MusicService.LocalBinder) service).getService();
            binding.volumeSeekBar.setValue((int)(musicService.getVolume() * 100));
        }

        public void onServiceDisconnected(ComponentName className) {
            musicService = null;
            isBound = false;
        }
    };

    private FragmentSettingsBinding binding;
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBindService();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View v = binding.getRoot();
        binding.settingsBtnStartMusic.setOnClickListener(view -> {
            musicService.playMusic();
            view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
        });

        binding.settingsBtnStopMusic.setOnClickListener(view -> {
            musicService.stopMusic();
            view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
        });
        binding.volumeSeekBar.addOnChangeListener((slider, value, fromUser) -> {
            float leftVolume = (float) (value / 100.0);
            float rightVolume = (float) (value / 100.0);
            musicService.setVolume(leftVolume, rightVolume);
        });
        return v;
    }

    void doBindService() {
        getActivity().bindService(new Intent(getActivity(),
                MusicService.class), mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}