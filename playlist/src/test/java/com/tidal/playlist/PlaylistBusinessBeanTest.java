package com.tidal.playlist;

import com.tidal.playlist.dao.PlaylistDaoBean;
import com.tidal.playlist.data.PlayListTrack;
import com.tidal.playlist.data.Track;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.testng.AssertJUnit.assertTrue;


public class PlaylistBusinessBeanTest {

    @BeforeMethod
    public void setUp() throws Exception {

    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testAddTracks() throws Exception {
        List<Track> trackList = new ArrayList<Track>();

        Track track = new Track();
        track.setArtistId(4);
        track.setTitle("A brand new track");
        track.setTrackNumberIdx(1);
        track.setId(76868);

        trackList.add(track);

        List<PlayListTrack> playListTracks = new PlaylistBusinessBean(new PlaylistDaoBean()).addTracks(UUID.randomUUID().toString(), 1, trackList, 5, new Date());

        assertTrue(playListTracks.size() > 0);
    }
}