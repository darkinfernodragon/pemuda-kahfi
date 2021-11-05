package net.atommobile.pemudakahfi.video;

import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

import net.atommobile.pemudakahfi.MainActivity;
import net.atommobile.pemudakahfi.R;

/**
 * Created by root on 27/05/16.
 */
public class VideoDetail extends Fragment implements YouTubePlayer.OnInitializedListener {

    String id_album;
    String judul;
    String waktu;
    String gambar;
    String keterangan;

    TextView lbl_judul;
    String id_topik,id_tipe;
    private Bundle data = null;

    private static final int RECOVERY_DIALOG_REQUEST = 10;
    public static String API_KEY;

    //From URL -> https://www.youtube.com/watch?v=kHue-HaXXzg
    // Let It Go : "Frozen"
    private String VIDEO_ID;
    private YouTubePlayer YPlayer;
    YouTubePlayerView youTubeView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_video_detail,container,false);

        LinearLayout layout_web = (LinearLayout) v.findViewById(R.id.layout_video_detail);
        ((MainActivity)getActivity()).setBackground(v, layout_web, getResources());

        judul = this.getArguments().getString("judul");
        VIDEO_ID = this.getArguments().getString("video_id");

        lbl_judul = (TextView) v.findViewById(R.id.lbl_judul);
        lbl_judul.setText(judul);


        API_KEY = getResources().getString(R.string.youtube_api_key);
        Log.e("video detail", "onCreateView: video id "+VIDEO_ID );
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_view, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    YPlayer = youTubePlayer;
                    YPlayer.setFullscreen(false);
//                    PlayerStyle style = PlayerStyle.MINIMAL;
//                    player.setPlayerStyle(style);
//Tell the player how to control the change
                    YPlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener(){
                        @Override
                        public void onFullscreen(boolean arg0) {
// do full screen stuff here, or don't. I started a YouTubeStandalonePlayer
// to go to full screen
                            YPlayer.setFullscreen(arg0);
                        }});

                    YPlayer.loadVideo(VIDEO_ID);
                    YPlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                // TODO Auto-generated method stub

            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            YPlayer = youTubePlayer;
            YPlayer.setFullscreen(false);
//Tell the player how to control the change
            YPlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener(){
                @Override
                public void onFullscreen(boolean arg0) {
// do full screen stuff here, or don't. I started a YouTubeStandalonePlayer
// to go to full screen
                    YPlayer.setFullscreen(arg0);
                }});
            YPlayer.loadVideo(VIDEO_ID);
            YPlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    private class FullScreenListener implements YouTubePlayer.OnFullscreenListener{

        @Override
        public void onFullscreen(boolean isFullscreen) {
            //Called when fullscreen mode changes.

        }

    }

    private class PlaybackListener implements YouTubePlayer.PlaybackEventListener{

        @Override
        public void onBuffering(boolean isBuffering) {
            // Called when buffering starts or ends.

        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to pause() or user action.

        }

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to play() or user action.

        }

        @Override
        public void onSeekTo(int newPositionMillis) {
            // Called when a jump in playback position occurs,
            //either due to the user scrubbing or a seek method being called

        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.

        }

    }

    private class PlayerStateListener implements YouTubePlayer.PlayerStateChangeListener{

        @Override
        public void onAdStarted() {
            // Called when playback of an advertisement starts.

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason reason) {
            // Called when an error occurs.

        }

        @Override
        public void onLoaded(String arg0) {
            // Called when a video has finished loading.

        }

        @Override
        public void onLoading() {
            // Called when the player begins loading a video and is not ready to accept commands affecting playback

        }

        @Override
        public void onVideoEnded() {
            // Called when the video reaches its end.

        }

        @Override
        public void onVideoStarted() {
            // Called when playback of the video starts.

        }

    }

    private class PlayListListener implements YouTubePlayer.PlaylistEventListener{

        @Override
        public void onNext() {
            // Called before the player starts loading the next video in the playlist.

        }

        @Override
        public void onPlaylistEnded() {
            // Called when the last video in the playlist has ended.

        }

        @Override
        public void onPrevious() {
            // Called before the player starts loading the previous video in the playlist.

        }

    }

}
