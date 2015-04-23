package com.tidal.playlist;

import com.tidal.playlist.dao.PlaylistDaoBean;
import com.tidal.playlist.data.PlayListTrack;
import com.tidal.playlist.data.Track;
import com.tidal.playlist.exception.PlaylistException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class PlaylistBusinessBeanTest {

    @Test
    public void addsSingleTrackToEmptyPlaylist() {
        List<Track> trackList = new ArrayList<Track>();

        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(100);

        trackList.add(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 0;
        List<PlayListTrack> playListTracks = new PlaylistBusinessBean(new PlaylistDaoBean(numTracksToGenerate), maxNumTracks)
                .addTracks(UUID.randomUUID().toString(), 1, trackList, 0, new Date());

        assertThat(playListTracks.size(), is(1));
        assertThat(playListTracks.get(0).getTrack().getId(), is(100));
    }

    @Test
    public void addsMultipleTracksToEmptyPlaylist() {
        List<Track> trackList = new ArrayList<Track>();

        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(100);
        trackList.add(track);

        track = new Track();
        track.setArtistId(5);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(101);
        trackList.add(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 0;
        List<PlayListTrack> playListTracks = new PlaylistBusinessBean(new PlaylistDaoBean(numTracksToGenerate), maxNumTracks)
                .addTracks(UUID.randomUUID().toString(), 1, trackList, 0, new Date());

        assertThat(playListTracks.size(), is(2));
        assertThat(playListTracks.get(0).getTrack().getId(), is(100));
        assertThat(playListTracks.get(1).getTrack().getId(), is(101));
    }

    @Test
    public void addsToBeginningOfPlaylist() {
        List<Track> trackList = new ArrayList<Track>();

        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(100);

        trackList.add(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        List<PlayListTrack> playListTracks = new PlaylistBusinessBean(new PlaylistDaoBean(numTracksToGenerate), maxNumTracks)
                .addTracks(UUID.randomUUID().toString(), 1, trackList, 0, new Date());

        assertThat(playListTracks.size(), is(3));
        assertThat(playListTracks.get(0).getTrack().getId(), is(100));
    }

    @Test
    public void addsToMiddleOfPlaylist() {
        List<Track> trackList = new ArrayList<Track>();

        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(100);

        trackList.add(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        List<PlayListTrack> playListTracks = new PlaylistBusinessBean(new PlaylistDaoBean(numTracksToGenerate), maxNumTracks)
                .addTracks(UUID.randomUUID().toString(), 1, trackList, 1, new Date());

        assertThat(playListTracks.size(), is(3));
        assertThat(playListTracks.get(1).getTrack().getId(), is(100));
    }

    @Test
    public void addsToEndOfPlaylist() {
        List<Track> trackList = new ArrayList<Track>();

        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(100);

        trackList.add(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        List<PlayListTrack> playListTracks = new PlaylistBusinessBean(new PlaylistDaoBean(numTracksToGenerate), maxNumTracks)
                .addTracks(UUID.randomUUID().toString(), 1, trackList, 2, new Date());

        assertThat(playListTracks.size(), is(3));
        assertThat(playListTracks.get(2).getTrack().getId(), is(100));
    }

    @Test(expected = PlaylistException.class)
    public void throwsExceptionWhenExceedsMaxNumTracks() {
        List<Track> trackList = new ArrayList<Track>();

        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(76868);

        trackList.add(track);

        int numTracksToGenerate = 10;
        int maxNumTracks = 10;
        new PlaylistBusinessBean(new PlaylistDaoBean(numTracksToGenerate), maxNumTracks)
                .addTracks(UUID.randomUUID().toString(), 1, trackList, 5, new Date());
    }

    @Test
    public void allowsAddingTracksUntilMaxNumTracks() {
        List<Track> trackList = new ArrayList<Track>();

        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(76868);

        trackList.add(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 5;
        new PlaylistBusinessBean(new PlaylistDaoBean(numTracksToGenerate), maxNumTracks)
                .addTracks(UUID.randomUUID().toString(), 1, trackList, 5, new Date());
    }
}