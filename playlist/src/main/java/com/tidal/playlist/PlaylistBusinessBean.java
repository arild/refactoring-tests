package com.tidal.playlist;

import com.tidal.playlist.dao.PlaylistDaoBean;
import com.tidal.playlist.data.PlayListTrack;
import com.tidal.playlist.data.Track;
import com.tidal.playlist.data.TrackPlayList;
import com.tidal.playlist.exception.PlaylistException;

import java.util.*;

public class PlaylistBusinessBean {

    private final int userId;
    private final String uuid;
    private PlaylistDaoBean playlistDaoBean;
    private int maxNumTracks;

    public PlaylistBusinessBean(int userId, String uuid, PlaylistDaoBean playlistDaoBean, int maxNumTracks) {
        this.userId = userId;
        this.uuid = uuid;
        this.playlistDaoBean = playlistDaoBean;
        this.maxNumTracks = maxNumTracks;
    }

    List<PlayListTrack> addTracks(List<Track> tracksToAdd, int toIndex, Date lastUpdated) throws PlaylistException {

        try {
            TrackPlayList playList = playlistDaoBean.getPlaylistByUUID(uuid, userId);

            if (isPlaylistFull(playList, tracksToAdd, maxNumTracks)) {
                throw new PlaylistException("Playlist cannot have more than " + maxNumTracks + " tracks");
            }

            toIndex = handleIndexOutOfBounds(toIndex, playList);

            if (!validateIndexes(playList, toIndex)) {
                throw new PlaylistException("Playlist index is invalid. Can not add to index " + toIndex);
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

    private int handleIndexOutOfBounds(int toIndex, TrackPlayList playList) {
        // The index is out of bounds, put it in the end of the list.
        if (toIndex > playList.getPlayListTracksSize() || toIndex == -1) {
            return playList.getPlayListTracksSize();
        }
        return toIndex;
    }

    private boolean isPlaylistFull(TrackPlayList playList, List<Track> tracksToAdd, int maxNumTracks) {
        return playList.getNrOfTracks() + tracksToAdd.size() > this.maxNumTracks;
    }

    private boolean validateIndexes(TrackPlayList playlist, int toIndex) {
        return toIndex <= playlist.getNrOfTracks() && toIndex >= 0;
    }

    private float addTrackDurationToPlaylist(TrackPlayList playList, Track track) {
        return (track != null ? track.getDuration() : 0)
                + (playList != null && playList.getDuration() != null ? playList.getDuration() : 0);
    }
}
