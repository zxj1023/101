package tran.com.android.gc.lib.media;

public interface VolumeController {

	public void postHasNewRemotePlaybackInfo();

    public void postRemoteVolumeChanged(int streamType, int flags);

    public void postRemoteSliderVisibility(boolean visible);
}
