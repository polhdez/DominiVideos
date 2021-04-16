package com.domain.videos;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Video {

	// Properties
	private enum uploadStatusEnum {
		UPLOADING, VERIFYING, PUBLIC
	}

	private enum playStatusEnum {
		STOPPED, PAUSED, PLAYING
	}

	private playStatusEnum playStatus;
	private uploadStatusEnum uploadStatus;
	private String url;
	private String title;
	private ArrayList<String> tags;
	private Date uploadDate;

	// Timer stuff
	private int duration; // in seconds
	private PlayingTask playingTask;
	private Timer playingTimer;
	private UploadingTask uploadingTask;
	private Timer uploadingTimer;
	private int seconds = 0;
	private int minutes = 0;
	private int hours = 0;
	private int totalSeconds;

	public Video(String title, ArrayList<String> tags, int duration) {
		this.title = title;
		this.tags = tags;
		this.uploadDate = new Date();
		this.duration = duration;
		this.playStatus = playStatusEnum.STOPPED;
		this.upload();
	}

	// Getters and setters
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) throws Exception {
		checkIfEmpty(url);
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) throws Exception {
		checkIfEmpty(title);
		this.title = title;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) throws Exception {
		for (String tag : tags)
			checkIfEmpty(tag);
		this.tags = tags;
	}

	public Date getUploadDateTime() {
		return uploadDate;
	}

	public void setUploadDateTime(Date uploadDateTime) {
		this.uploadDate = uploadDateTime;
	}

	// Methods
	public void upload() {
		System.out.println("[*] Video is uploading...");
		this.uploadingTask = new UploadingTask(this);
		this.uploadingTimer = new Timer();
		this.uploadingTimer.scheduleAtFixedRate(this.uploadingTask, 0, 1000);
	}

	public void play() {
		if (this.uploadStatus == uploadStatusEnum.PUBLIC && (this.playStatus == playStatusEnum.STOPPED || this.playStatus == playStatusEnum.PAUSED)) {
			this.playingTask = new PlayingTask(this);
			this.playingTimer = new Timer();
			this.playingTimer.scheduleAtFixedRate(this.playingTask, 0, 1000);
			this.playStatus = playStatusEnum.PLAYING;
			System.out.println("[*] Video is playing...");
		}
		if(this.uploadStatus == uploadStatusEnum.UPLOADING || this.uploadStatus == uploadStatusEnum.VERIFYING) {
			System.out.println("[!] Wait, video is still " + this.uploadStatus);
		}
	}

	public void stop() {
		if (this.uploadStatus == uploadStatusEnum.PUBLIC && (this.playStatus == playStatusEnum.PAUSED || this.playStatus == playStatusEnum.PLAYING)) {
			this.playingTask.cancel();
			this.totalSeconds = 0;
			this.seconds = 0;
			this.minutes = 0;
			this.hours = 0;			
			this.playStatus = playStatusEnum.STOPPED;
			System.out.println("[*] Video is stopped...");
		}
	}

	public void pause() {
		if (this.uploadStatus == uploadStatusEnum.PUBLIC && this.playStatus == playStatusEnum.PLAYING) {
			this.playingTask.cancel();
			this.playStatus = playStatusEnum.PAUSED;
			System.out.println("[*] Video is paused...");
		}
	}

	private void checkIfEmpty(String str) throws Exception {
		if (str.isEmpty()) {
			throw new EmptyStringException("[!] " + str + " is empty!");
		}
	}

	// Uploading time timer
	private class UploadingTask extends TimerTask {

		private Video video;
		private int totalSeconds;

		public UploadingTask(Video video) {
			this.video = video;
		}

		public void run() {
			
			if (totalSeconds < 10) {
				video.uploadStatus = uploadStatusEnum.UPLOADING;
			}
			if (totalSeconds < 15) {
				video.uploadStatus = uploadStatusEnum.VERIFYING;
			}
			if (totalSeconds >= 15) {
				video.uploadStatus = uploadStatusEnum.PUBLIC;
				System.out.println("[*] Video finished uploading!");
				this.cancel();
			}
			totalSeconds++;
		}
	}

	// Playing timer class
	private class PlayingTask extends TimerTask {

		private Video video;

		public PlayingTask(Video video) {
			this.video = video;
		}
		
		private String clockNum(int num) {
			String numString = Integer.toString(num);
			if(numString.length() == 1) {
				return "0" + numString;
			}
			return numString;
		}

		public void run() {
			if (video.totalSeconds == video.duration) {
				video.totalSeconds = 0;
				video.seconds = 0;
				video.minutes = 0;
				video.hours = 0;
				video.playStatus = playStatusEnum.STOPPED;
				System.out.println("[*] Video finished playing!");				
				this.cancel();
				return;
			}

			if (video.seconds == 59) {
				video.seconds = 0;
				video.minutes++;
			}
			if (video.minutes == 59) {
				video.hours++;
			}
			
			System.out.println("Playing for: " + clockNum(video.hours) + ":" + clockNum(video.minutes) + ":" + clockNum(video.seconds));
			video.seconds++;
			video.totalSeconds++;
		}
	}
}
