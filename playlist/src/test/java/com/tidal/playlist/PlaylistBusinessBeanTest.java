package com.tidal.playlist;

import com.tidal.playlist.dao.PlaylistDaoBean;
import com.tidal.playlist.data.PlayListTrack;
import com.tidal.playlist.data.Track;
import com.tidal.playlist.exception.PlaylistException;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class PlaylistBusinessBeanTest {

    @Test
    public void addsSingleTrackToEmptyPlaylist() {
        Track track = track();
        track.setId(100);
        List<Track> trackList = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 0;
        List<PlayListTrack> playListTracks = playlistBean(maxNumTracks, numTracksToGenerate)
                .addTracks(trackList, 0, new Date());

        assertThat(playListTracks.size(), is(1));
        assertThat(playListTracks.get(0).getTrack().getId(), is(100));
    }

    @Test
    public void addsMultipleTracksToEmptyPlaylist() {
        Track track1 = track();
        track1.setId(100);

        Track track2 = track();
        track2.setId(101);

        List<Track> trackList = asList(track1, track2);

        int maxNumTracks = 10;
        int numTracksToGenerate = 0;
        List<PlayListTrack> playListTracks = playlistBean(maxNumTracks, numTracksToGenerate)
                .addTracks(trackList, 0, new Date());

        assertThat(playListTracks.size(), is(2));
        assertThat(playListTracks.get(0).getTrack().getId(), is(100));
        assertThat(playListTracks.get(1).getTrack().getId(), is(101));
    }

    @Test
    public void addsToBeginningOfPlaylist() {
        Track track = track();
        track.setId(100);
        List<Track> trackList = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        List<PlayListTrack> playListTracks = playlistBean(maxNumTracks, numTracksToGenerate)
                .addTracks(trackList, 0, new Date());

        assertThat(playListTracks.size(), is(3));
        assertThat(playListTracks.get(0).getTrack().getId(), is(100));
    }

    @Test
    public void addsToMiddleOfPlaylist() {
        Track track = track();
        track.setId(100);
        List<Track> trackList = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        List<PlayListTrack> playListTracks = playlistBean(maxNumTracks, numTracksToGenerate)
                .addTracks(trackList, 1, new Date());

        assertThat(playListTracks.size(), is(3));
        assertThat(playListTracks.get(1).getTrack().getId(), is(100));
    }

    @Test
    public void addsToEndOfPlaylist() {
        Track track = track();
        track.setId(100);
        List<Track> trackList = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        List<PlayListTrack> playListTracks = playlistBean(maxNumTracks, numTracksToGenerate)
                .addTracks(trackList, 2, new Date());

        assertThat(playListTracks.size(), is(3));
        assertThat(playListTracks.get(2).getTrack().getId(), is(100));
    }

    @Test(expected = PlaylistException.class)
    public void throwsExceptionWhenExceedsMaxNumTracks() {
        List<Track> trackList = asList(track());

        int numTracksToGenerate = 10;
        int maxNumTracks = 10;
        playlistBean(maxNumTracks, numTracksToGenerate)
                .addTracks(trackList, 1, new Date());
    }

    @Test
    public void allowsAddingTracksUntilMaxNumTracks() {
        List<Track> trackList = asList(track());

        int maxNumTracks = 10;
        int numTracksToGenerate = 5;
        playlistBean(maxNumTracks, numTracksToGenerate).addTracks(trackList, 5, new Date());
    }

    @Test(expected = PlaylistException.class)
    public void throwsExceptionWhenAddingToNegativeIndex() throws Exception {
        List<Track> trackList = asList(track());

        int maxNumTracks = 10;
        int numTracksToGenerate = 5;
        playlistBean(maxNumTracks, numTracksToGenerate).addTracks(trackList, -10, new Date());

    }

    @Test
    public void addsToEndOfPlaylistWhenIndexIsLargerThanMaxNumTracks() throws Exception {
        Track track = track();
        track.setId(100);
        List<Track> trackList = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 5;
        List<PlayListTrack> playListTracks = playlistBean(maxNumTracks, numTracksToGenerate)
                .addTracks(trackList, 20, new Date());

        assertThat(playListTracks.size(), is(6));
        assertThat(playListTracks.get(5).getTrack().getId(), is(100));

    }

    private Track track() {
        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(100);
        return track;
    }

    private PlaylistBusinessBean playlistBean(int maxNumTracks, int numTracksToGenerate) {
        return new PlaylistBusinessBean(1, UUID.randomUUID().toString(), new PlaylistDaoBean(numTracksToGenerate), maxNumTracks);
    }
}