package com.tidal.playlist;

import com.tidal.playlist.dao.PlaylistDaoBean;
import com.tidal.playlist.data.PlayListTrack;
import com.tidal.playlist.data.Track;
import com.tidal.playlist.data.TrackPlayList;
import com.tidal.playlist.exception.PlaylistException;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class PlaylistBusinessBeanTest {

    @Test
    public void addsSingleTrackToEmptyPlaylist() {
        Track track = track();
        track.setId(100);
        List<Track> tracks = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 0;
        int toIndex = 0;
        List<PlayListTrack> playListTracks = playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .addTracks(tracks, toIndex, new Date()).getPlayList().getPlayListTracksSorted();

        assertThat(playListTracks.size(), is(1));
        assertThat(playListTracks.get(0).getTrack().getId(), is(100));
        assertThat(playListTracks.get(0).getIndex(), is(0));
    }

    @Test
    public void addsMultipleTracksToEmptyPlaylist() {
        Track track1 = track();
        track1.setId(100);

        Track track2 = track();
        track2.setId(101);

        List<Track> tracks = asList(track1, track2);

        int maxNumTracks = 10;
        int numTracksToGenerate = 0;
        int toIndex = 0;
        List<PlayListTrack> playListTracks = playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .addTracks(tracks, toIndex, new Date()).getPlayList().getPlayListTracksSorted();

        assertThat(playListTracks.size(), is(2));

        assertThat(playListTracks.get(0).getTrack().getId(), is(100));
        assertThat(playListTracks.get(0).getIndex(), is(0));

        assertThat(playListTracks.get(1).getTrack().getId(), is(101));
        assertThat(playListTracks.get(1).getIndex(), is(1));
    }

    @Test
    public void addsToBeginningOfPlaylist() {
        Track track = track();
        track.setId(100);
        List<Track> tracks = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        int toIndex = 0;
        List<PlayListTrack> playListTracks = playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .addTracks(tracks, toIndex, new Date()).getPlayList().getPlayListTracksSorted();

        assertThat(playListTracks.size(), is(3));
        assertThat(playListTracks.get(0).getTrack().getId(), is(100));
        assertThat(playListTracks.get(0).getIndex(), is(0));
    }

    @Test
    public void addsToMiddleOfPlaylist() {
        Track track = track();
        track.setId(100);
        List<Track> tracks = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        int toIndex = 1;
        List<PlayListTrack> playListTracks = playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .addTracks(tracks, toIndex, new Date()).getPlayList().getPlayListTracksSorted();

        assertThat(playListTracks.size(), is(3));
        assertThat(playListTracks.get(1).getTrack().getId(), is(100));
        assertThat(playListTracks.get(1).getIndex(), is(1));
    }

    @Test
    public void addsToEndOfPlaylist() {
        Track track = track();
        track.setId(100);
        List<Track> tracks = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        int toIndex = 2;
        List<PlayListTrack> playListTracks = playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .addTracks(tracks, toIndex, new Date()).getPlayList().getPlayListTracksSorted();

        assertThat(playListTracks.size(), is(3));
        assertThat(playListTracks.get(2).getTrack().getId(), is(100));
        assertThat(playListTracks.get(2).getIndex(), is(2));
    }

    @Test
    public void addsToEndOfPlaylistWhenIndexIsLargerThanMaxNumTracks() throws Exception {
        Track track = track();
        track.setId(100);
        List<Track> tracks = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 5;
        int toIndex = 20;
        List<PlayListTrack> playListTracks = playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .addTracks(tracks, toIndex, new Date()).getPlayList().getPlayListTracksSorted();

        assertThat(playListTracks.size(), is(6));
        assertThat(playListTracks.get(5).getTrack().getId(), is(100));
    }

    @Test
    public void updatesDurationOnPlaylistWhenAddingTrack() throws Exception {
        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        PlaylistBusinessBean playListBean = playlistBusinessBean(maxNumTracks, numTracksToGenerate);

        Float durationBefore = playListBean.getPlayList().getDuration();

        Track track = track();
        track.setId(100);
        track.setDuration(2.f);
        List<Track> tracks = asList(track);

        int toIndex = 1;
        Float duration = playListBean.addTracks(tracks, toIndex, new Date())
                .getPlayList()
                .getDuration();

        assertEquals(durationBefore + 2.f, duration, 0.1f);
    }

    @Test
    public void allowsAddingTracksUntilMaxNumTracks() {
        List<Track> tracks = asList(track());

        int maxNumTracks = 10;
        int numTracksToGenerate = 5;
        int toIndex = 5;
        playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .addTracks(tracks, toIndex, new Date()).getPlayList().getPlayListTracksSorted();
    }

    @Test(expected = PlaylistException.class)
    public void throwsExceptionWhenAddingToNegativeIndex() throws Exception {
        List<Track> tracks = asList(track());

        int maxNumTracks = 10;
        int numTracksToGenerate = 5;
        int toIndex = -10;
        playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .addTracks(tracks, toIndex, new Date()).getPlayList().getPlayListTracksSorted();
    }

    @Test
    public void updatesNumberOfTracksAttributeInPlaylistWhenAddingTrack() {
        Track track = track();
        track.setId(100);
        List<Track> tracks = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        int toIndex = 2;
        PlaylistBusinessBean playlistBusinessBean = playlistBusinessBean(maxNumTracks, numTracksToGenerate);

        int nrOfTracksBefore = playlistBusinessBean.getPlayList().getNrOfTracks();

        TrackPlayList playList = playlistBusinessBean
                .addTracks(tracks, toIndex, new Date()).getPlayList();

        assertThat(nrOfTracksBefore, is(2));
        assertThat(playList.getNrOfTracks(), is(3));
    }

    @Test
    public void deleteHandlesEmptyList() throws Exception {
        List<Integer> trackIds = asList(1);

        int maxNumTracks = 10;
        int numTracksToGenerate = 0;
        List<PlayListTrack> playListTracks = playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .deleteTracks(trackIds)
                .getPlayList()
                .getPlayListTracksSorted();

        assertThat(playListTracks.size(), is(0));
    }

    @Test
    public void deletesTrackFromSingleTrackPlaylist() throws Exception {
        List<Integer> trackIds = asList(1);

        int maxNumTracks = 10;
        int numTracksToGenerate = 1;
        List<PlayListTrack> playListTracks = playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .deleteTracks(trackIds)
                .getPlayList()
                .getPlayListTracksSorted();

        assertThat(playListTracks.size(), is(0));
    }

    @Test
    public void updatesDurationOnPlaylistWhenDeletingTrack() throws Exception {
        Track track = track();
        track.setId(100);
        track.setDuration(2.f);
        List<Track> tracks = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 2;
        PlaylistBusinessBean playListBean = playlistBusinessBean(maxNumTracks, numTracksToGenerate);

        int toIndex = 2;
        TrackPlayList playList = playListBean.addTracks(tracks, toIndex, new Date())
                .getPlayList();

        Integer playListTrackId = playList.getPlayListTracksSorted().get(2).getId();
        Float durationBefore = playList.getDuration();

        Float durationAfter = playListBean.deleteTracks(asList(playListTrackId))
                .getPlayList()
                .getDuration();

        assertEquals(durationBefore - 2.f, durationAfter, 0.1f);
    }

    @Test
    public void deletesMultipleTracksFromPlaylist() throws Exception {
        List<Integer> trackIds = asList(1, 2, 3, 7, 10);

        int maxNumTracks = 10;
        int numTracksToGenerate = 10;
        List<PlayListTrack> playListTracks = playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .deleteTracks(trackIds)
                .getPlayList()
                .getPlayListTracksSorted();

        assertThat(playListTracks.size(), is(5));

        assertThat(playListTracks.get(0).getIndex(), is(0));
        assertThat(playListTracks.get(1).getIndex(), is(1));
        assertThat(playListTracks.get(2).getIndex(), is(2));
        assertThat(playListTracks.get(3).getIndex(), is(3));
        assertThat(playListTracks.get(4).getIndex(), is(4));
    }

    @Test
    public void setsAttributesOnPlayListTrack() throws Exception {
        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("test track");
        track.setTrackNumberIdx(10);
        track.setId(100);
        track.setDuration(1.f);

        List<Track> tracks = asList(track);

        int maxNumTracks = 10;
        int numTracksToGenerate = 0;
        int toIndex = 0;
        Date lastUpdated = new Date();
        List<PlayListTrack> playListTracks = playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .addTracks(tracks, toIndex, lastUpdated).getPlayList().getPlayListTracksSorted();
        PlayListTrack playListTrack = playListTracks.get(0);

        assertThat(playListTrack.getTrackArtistId(), is(track.getArtistId()));
        assertThat(playListTrack.getDateAdded().equals(lastUpdated), is(true));
        assertThat(playListTrack.getTrack().getId(), is(track.getId()));
    }

    @Test(expected = PlaylistException.class)
    public void throwsExceptionWhenExceedsMaxNumTracks() {
        List<Track> tracks = asList(track());

        int numTracksToGenerate = 10;
        int maxNumTracks = 10;
        int toIndex = 1;
        playlistBusinessBean(maxNumTracks, numTracksToGenerate)
                .addTracks(tracks, toIndex, new Date()).getPlayList().getPlayListTracksSorted();
    }

    private Track track() {
        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(100);
        track.setDuration(1.f);
        return track;
    }

    private PlaylistBusinessBean playlistBusinessBean(int maxNumTracks, int numTracksToGenerate) {
        return new PlaylistBusinessBean(1, UUID.randomUUID().toString(), playlistDaoBean(numTracksToGenerate), maxNumTracks);
    }

    private PlaylistDaoBean playlistDaoBean(int numTracksToGenerate) {
        return new PlaylistDaoBean(numTracksToGenerate);
    }
}