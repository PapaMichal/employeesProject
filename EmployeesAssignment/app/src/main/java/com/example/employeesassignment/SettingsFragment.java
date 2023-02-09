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
import android.widget.SeekBar;
import com.example.employeesassignment.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {
    MusicService musicService;
    boolean isBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            musicService = ((MusicService.LocalBinder) service).getService();
            binding.volumeSeekBar.setProgress((int)(musicService.getVolume() * 100));
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
        binding.settingsBtnStartMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.playMusic();
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
            }
        });

        binding.settingsBtnStopMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicService.stopMusic();
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.image_click));
            }
        });
        binding.volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float leftVolume = (float) (progress / 100.0);
                float rightVolume = (float) (progress / 100.0);
                musicService.setVolume(leftVolume, rightVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        return v;
    }

    void doBindService() {
        getActivity().bindService(new Intent(getActivity(),
                MusicService.class), mConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    void doUnbindService() {
        if (isBound) {
            // Detach our existing connection.
            getActivity().unbindService(mConnection);
            isBound = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}