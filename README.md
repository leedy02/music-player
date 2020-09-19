# music-player

-Project: Music Player 

-Developer : Lee Eui Seok, Chun Seung Whan
			
-Eclipse Version: 2019-03 (4.11.0)


##Playing Song Functions

1. Play: play song and load current position if it was paused state

2. Pause: pause song and keep current position of song
 
3. Stop: stop current song and get back to the start of the song

4. Next: load next song. if current song is end, get to the start of the list

5. Prev: load previous song. if current song is start, get to the end of the list

* DoubleClicklistVIew: double click song you want to play from the list view


##Playlist Functions

1. Show the songs in the playlist: done by making a dedicated Pane
-[Show the title, artist and duration of each song]
-[The list of the song can be scrollable by ListView]
-[Can select multiple song from the list to remove them using one click: How-To: 1.ctrl-click music you want to remove. 2.Go to menu. 3.Remove Song]

2. Open File: if choose mp3 file and open then creates new txt playlist and load to list view and mediaplayer

3. Add Song: can choose multiple music and load. loads to the end of the playlist.

4. Remove Song: choose and remove multiple songs

5. New Playlist: create new txt file. We have limited maximum txt file in the directory to 10

6. Open Playlist: open an existing txt file and use it right away. pre-made txt file exist to try out function

7. Save the File: saves the file as we add, remove new, open ec cetra.

8. Remove Playlist: delete a txt file


##Extra Feature

1.  Used JavaFX and SceneBuilder2, along with recommended JFoenix.jar external library and JAudioTagger.2.2.3.jar to get Audio file's metadata. All jar files are included within the ZIP file. please Add External JARs.

2. We implemented exter file extentions like  (*.wav) other than *.mp3

3. We made a Volume controller that runs smoothly

4. We made a sliderbar that shows progress of the current playing music.

5. We used the scrollTo() function of the ListView library which brings up the music currently running in the list view to the top. 

6. We created function to doubleclick on a listitem to play that music

7. The playlist is saved automatically and forms .txt file.

*. Pops up extra error window to show what error has occured

