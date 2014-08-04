package org.torpedoquery.jpa.test.bo;

public class ProjectionEntity {

	private String test1;
	private Integer test2;

	public ProjectionEntity(String test1, Integer test2) {
		this.setTest1(test1);
		this.setTest2(test2);
	}

	public String getTest1() {
		return test1;
	}

	public void setTest1(String test1) {
		this.test1 = test1;
	}

	public Integer getTest2() {
		return test2;
	}

	public void setTest2(Integer test2) {
		this.test2 = test2;
	}

}
