package com.tidal.playlist;

import com.tidal.playlist.dao.PlaylistDaoBean;
import com.tidal.playlist.data.PlayListTrack;
import com.tidal.playlist.data.Track;
import com.tidal.playlist.data.TrackPlayList;
import com.tidal.playlist.exception.PlaylistException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class PlaylistBusinessBean {

    private final TrackPlayList playList;
    private int maxNumTracks;

    public PlaylistBusinessBean(int userId, String uuid, PlaylistDaoBean playlistDaoBean, int maxNumTracks) {
        this.maxNumTracks = maxNumTracks;

        // This would normally be called for each operation modifying the playlist, .e.g addTracks()
        // At the end of each such operation, there would also be storing operation.
        // For now, just load the playlist and modify it in memory.
        playList = playlistDaoBean.getPlaylistByUUID(uuid, userId);
    }

    public TrackPlayList getPlayList() {
        return playList;
    }

    PlaylistBusinessBean addTracks(List<Track> tracksToAdd, int toIndex, Date lastUpdated) throws PlaylistException {
        if (isPlaylistFull(playList, tracksToAdd, maxNumTracks)) {
            throw new PlaylistException("Playlist cannot have more than " + maxNumTracks + " tracks");
        }
        toIndex = handleIndexOutOfBounds(playList, toIndex);

        if (!isValidIndex(playList, toIndex)) {
            throw new PlaylistException("Playlist index is invalid. Can not add to index " + toIndex);
        }

        List<PlayListTrack> original = getOriginalPlayListTracksSorted(playList);
        for (Track track : tracksToAdd) {
            original.add(toIndex, createPlayListTrack(playList, track, lastUpdated));
            playList.setDuration(addTrackDurationToPlaylist(playList, track));
            toIndex++;
        }

        int i = 0;
        for (PlayListTrack track : original) {
            track.setIndex(i++);
        }

        playList.getPlayListTracks().clear();
        playList.getPlayListTracks().addAll(original);
        playList.setNrOfTracks(original.size());

        return this;
    }

    PlaylistBusinessBean deleteTracks(List<Integer> playListTrackIdsToDelete) {
        for (Integer id : playListTrackIdsToDelete) {
            deleteTrack(playList.getPlayListTracks(), id);
        }

        return this;
    }

    private static void deleteTrack(Set<PlayListTrack> playListTracks, final Integer playListTrackIdsToDelete) {
        for (PlayListTrack playListTrack : playListTracks) {
            if (playListTrack.getId() == playListTrackIdsToDelete) {
                playListTracks.remove(playListTrack);
                return;
            }
        }
    }

    private PlayListTrack createPlayListTrack(TrackPlayList playList, Track track, Date lastUpdated) {
        PlayListTrack playlistTrack = new PlayListTrack();
        playlistTrack.setTrack(track);
        playlistTrack.setTrackPlaylist(playList);
        playlistTrack.setTrackArtistId(track.getArtistId());
        playlistTrack.setDateAdded(lastUpdated);
        return playlistTrack;
    }

    private static List<PlayListTrack> getOriginalPlayListTracksSorted(TrackPlayList playList) {
        List<PlayListTrack> original = playList.getPlayListTracksSorted();
        if (original == null) {
            return new ArrayList<PlayListTrack>();
        }
        else {
            return original;
        }
    }

    private static int handleIndexOutOfBounds(TrackPlayList playList, int index) {
        // The index is out of bounds, put it in the end of the list.
        if (index > playList.getPlayListTracksSize() || index == -1) {
            return playList.getPlayListTracksSize();
        }
        return index;
    }

    private static boolean isPlaylistFull(TrackPlayList playList, List<Track> tracksToAdd, int maxNumTracks) {
        return playList.getNrOfTracks() + tracksToAdd.size() > maxNumTracks;
    }

    private static boolean isValidIndex(TrackPlayList playlist, int index) {
        return index <= playlist.getNrOfTracks() && index >= 0;
    }

    private static float addTrackDurationToPlaylist(TrackPlayList playList, Track track) {
        return (track != null ? track.getDuration() : 0)
                + (playList != null && playList.getDuration() != null ? playList.getDuration() : 0);
    }
}
