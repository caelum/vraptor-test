package br.com.caelum.vraptor.test.models;

public class Task {

	private String description;
	private int difficulty;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	@Override
	public String toString() {
		return "Task [description=" + description + ", difficulty=" + difficulty + "]";
	}
	
	

}
