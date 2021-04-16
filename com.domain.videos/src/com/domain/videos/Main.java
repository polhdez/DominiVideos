package com.domain.videos;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

	private static Scanner sc = new Scanner(System.in);
	private static HashMap<String, User> users = new HashMap<String, User>();

	public static void main(String[] args) throws Exception {

		Boolean asking = true;
		while (asking) {
			welcomeMenu();
		}
	}

	private static void welcomeMenu() {
		System.out.println("[*] Welcome to JavaVideos!\n");

		Boolean asking = true;
		while (asking) {
			String input = getInput("Do you want to (login) or (register)?");
			switch (input) {
			case "login":
				loginMenu();
				asking = false;
				break;
			case "register":
				registerMenu();
				asking = false;
				break;
			default:
				System.out.println("[!] Please enter a valid choice!");
			}
		}
	}

	private static void registerMenu() {
		System.out.println("[*] Let's create a user!");
		Boolean asking = true;
		while (asking) {
			String email = getInput("Enter your email: ");
			String name = getInput("Enter your name: ");
			String surnames = getInput("Enter your surnames: ");
			String password = getInput("Enter your password: ");
			try {
				User user = new User(email, name, surnames, password);
				users.put(email, user);
				asking = false;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private static void loginMenu() {
		System.out.println("[*] Let's login!");
		Boolean asking = true;
		while (asking) {
			String email = getInput("Enter your email: ");
			User user = users.get(email);
			if (user != null) {
				String password = getInput("Enter your password: ");
				if (password.equals(user.getPassword())) {
					asking = false;
					System.out.println("[*] Logged in succesfully!");
					userMenu(user);
				} else {
					System.out.println("[!] Wrong password, try again!");
					if (quitDialog())
						asking = false;
				}
			} else {
				System.out.println("[!] This user doesn't exist, try again!");
				if (quitDialog())
					asking = false;
			}
		}
	}

	private static void userMenu(User user) {
		System.out.println("[*] Welcome " + user.getName() + " " + user.getSurnames() + "!\n");

		Boolean asking = true;
		while (asking) {
			String input = getInput("Wanna (create) or (play) a video? To exit press (q)");
			switch (input) {
			case "create":
				createVideoMenu(user);
				break;
			case "play":
				try {
					Video video = selectVideoMenu(user);
					try {
						videoPlayer(video);
					} catch (Exception e) {
						System.out.println("Couldn't play the video because: " + e);
					}
				} catch (Exception e) {
					System.out.println("[!] You need to create  a video before playing!");
					createVideoMenu(user);
				}
				break;
			case "q":
				asking = false;
				break;
			default:
				System.out.println("[!] Please enter a valid choice!");
			}
		}
	}

	private static void videoPlayer(Video video) {
		Boolean asking = true;
		while (asking) {
			String input = getInput("[*] Type (play), (pause), (stop) or (q) to exit:");
			switch (input) {
			case "play":
				video.play();
				break;
			case "pause":
				video.pause();
				break;
			default:
				System.out.println("[!] Type a valid option!");
				break;
			case "q":
				video.stop();
				asking = false;
				break;
			case "stop":
				video.stop();
				break;
			}
		}

	}

	private static void createVideoMenu(User user) {
		System.out.println("[*] Input your video info: ");

		Video video;

		Boolean asking = true;
		while (asking) {
			int duration = 0;
			String title = getInput("Video title: ");
			ArrayList<String> tags = new ArrayList<String>(
					Arrays.asList(getInput("Video tags (sepparate them with ','): ").split(",")));
			Boolean askingDuration = true;
			while (askingDuration) {
				try {
					duration = Integer.parseInt(getInput("Enter the video duration in seconds: "));
					askingDuration = false;
				} catch (NumberFormatException e) {
					System.out.println("Enter a valid duration please!");
				}
			}
			video = new Video(title, tags, duration);
			user.addUserVideo(video);
			asking = false;
		}
	}

	private static Video selectVideoMenu(User user) throws Exception {
		Video video = null;
		Boolean asking = true;
		while (asking) {
			if (user.getUserVideos().size() == 0) {
				System.out.println("[!] You don't have any video!");
				throw new Exception();
			} else {
				System.out.println("[*] Your videos:");
				for (int i = 0; i < user.getUserVideos().size(); i++) {
					video = user.getUserVideos().get(i);
					System.out.println("[" + i + "] Title: " + video.getTitle() + " Tags: " + video.getTags());
				}
				try {
					int selection = Integer
							.parseInt(getInput("Select a video (0-" + (user.getUserVideos().size() - 1) + "): "));
					if (selection <= user.getUserVideos().size()) {
						video = user.getUserVideos().get(selection);
						System.out.println("[*] " + video.getTitle() + " selected!");
						asking = false;
					} else {
						System.out.println("[!] Enter a number in range please!");
					}
				} catch (NumberFormatException e) {
					System.out.println("[!] Enter a number please!");
				}
			}
		}
		return video;
	}

	private static boolean quitDialog() {
		if (getInput("Press (q) to quit or press any key to continue.").equals("q")) {
			return true;
		} else {
			return false;
		}
	}

	private static String getInput(String question) {
		System.out.println(question);
		return sc.nextLine();
	}
}
