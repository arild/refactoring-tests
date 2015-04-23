package com.tidal.playlist;

import com.tidal.playlist.dao.PlaylistDaoBean;
import com.tidal.playlist.data.PlayListTrack;
import com.tidal.playlist.data.Track;
import com.tidal.playlist.data.TrackPlayList;
import com.tidal.playlist.exception.PlaylistException;

import java.util.*;

public class PlaylistBusinessBean {

    private PlaylistDaoBean playlistDaoBean;
    private int maxNumTracks;

    public PlaylistBusinessBean(PlaylistDaoBean playlistDaoBean, int maxNumTracks) {
        this.playlistDaoBean = playlistDaoBean;
        this.maxNumTracks = maxNumTracks;
    }

    List<PlayListTrack> addTracks(String uuid, int userId, List<Track> tracksToAdd, int toIndex,
                                  Date lastUpdated) throws PlaylistException {

        try {

            TrackPlayList playList = playlistDaoBean.getPlaylistByUUID(uuid, userId);

            if (playList.getNrOfTracks() + tracksToAdd.size() > maxNumTracks) {
                throw new PlaylistException("Playlist cannot have more than " + maxNumTracks + " tracks");
            }

            // The index is out of bounds, put it in the end of the list.
            if (toIndex > playList.getPlayListTracksSize() || toIndex == -1) {
                toIndex = playList.getPlayListTracksSize();
            }

            if (!validateIndexes(toIndex, playList.getNrOfTracks())) {
                return Collections.EMPTY_LIST;
            }

            Set<PlayListTrack> originalSet = playList.getPlayListTracks();
            List<PlayListTrack> original;
            if (originalSet == null || originalSet.size() == 0)
                original = new ArrayList<PlayListTrack>();
            else
                original = new ArrayList<PlayListTrack>(originalSet);

            Collections.sort(original);

            for (Track track : tracksToAdd) {
                PlayListTrack playlistTrack = new PlayListTrack();
                playlistTrack.setTrack(track);
                playlistTrack.setTrackPlaylist(playList);
                playlistTrack.setTrackArtistId(track.getArtistId());
                playlistTrack.setDateAdded(lastUpdated);
                playList.setDuration(addTrackDurationToPlaylist(playList, track));
                original.add(toIndex, playlistTrack);
                toIndex++;
            }

            int i = 0;
            for (PlayListTrack track : original) {
                track.setIndex(i++);
            }

            playList.getPlayListTracks().clear();
            playList.getPlayListTracks().addAll(original);
            playList.setNrOfTracks(original.size());

            return original;

        } catch (Exception e) {
            e.printStackTrace();
            throw new PlaylistException("Generic error");
        }
    }

    private boolean validateIndexes(int toIndex, int length) {
        return toIndex >= 0 && toIndex <= length;
    }

    private float addTrackDurationToPlaylist(TrackPlayList playList, Track track) {
        return (track != null ? track.getDuration() : 0)
                + (playList != null && playList.getDuration() != null ? playList.getDuration() : 0);
    }
}
