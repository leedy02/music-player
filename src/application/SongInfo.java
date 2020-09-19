/*CSE 12141599 LEE EUI SEOK, 12150754 Chun Seung Whan
 * Java Programming (Tamer Professor)
 * Java Course Project (SongInfo.java)
 * Reporting Date : 2019.06.16
 */

// this is for Song Information we can see this thing in music file property
// if property is not filled then it will show various artist, various song
package application;


public class SongInfo {
	
	
	private String songName;
	private String artistName;
	private String songPath;
	private String duration;
	private double dur;
	
	public String getSongName() {
		return songName;
	}
	
	public SongInfo(String songName, String artistName,  String songPath,String duration,double dur) {
		super();
		setSongName(songName);
		setArtistName(artistName);
		this.songPath =songPath;
		this.duration = duration;
		this.dur=dur;
	}//constructor
	

	public void setSongPath(String songPath) {
		this.songPath = songPath;
	}
	
	public String getSongPath() {
		return songPath;
	}

	public double getDur() {
		return dur;
	}

	public void setSongName(String songName) {
		if(songName==null ||songName.equals(""))
		{
			this.songName = "Various Song";
		}
		else
		{
			this.songName =songName;
		}
	}
	
	public String getArtistName() {
		return artistName;
	}
	
	public void setArtistName(String artistName) {
		if(artistName==null ||artistName.equals(""))
		{
			this.artistName = "Various Artist";
		}
		else
		{
			this.artistName =artistName;
		}
	}
	
    public String toString()
    {
		return this.songName+";"+this.artistName+";"+this.songPath;
    }//this is for playlist in txt file but we just saved path only
    
    public String toString2()
    {
		return this.songName+" "+this.artistName+" "+this.duration;
    }//this is for playlist in GUI 
}
