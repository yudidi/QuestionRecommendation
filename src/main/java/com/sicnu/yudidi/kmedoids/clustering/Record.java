package com.sicnu.yudidi.kmedoids.clustering;

public class Record {

	private boolean isMedoid = false; // 是否是中心记录
	private int id; // 记录的编号 //方便矩阵中查找相异度
	private int[] users; // 参与讨论的用户id列表
	private String name; // 记录的名字,比如某个题目的id

	public Record(int id, String recordName, int[] users) {
		super();
		this.id = id;
		this.users = users;
		this.name = recordName;
	}

	/**
	 * 计算该记录和其他记录的相异度/距离. (距离越小,相似度越高,相异度越小)
	 * 
	 * @param record
	 * @return 1000 - maxLen
	 */
	public int calculateDistance(Record record) {
		int[] s1 = users;
		int[] s2 = record.users;
		int maxLen = 0;
		// MaxLCSLen算法
		int[][] dp = new int[s1.length][s2.length];
		if (s1[0] == s2[0]) {
			dp[0][0] = 1;
		}
		for (int i = 1; i < s1.length; i++) {
			if (s1[i] == s2[0]) {
				dp[i][0] = 1;
			} else {
				dp[i][0] = dp[i - 1][0];
			}
		}
		for (int i = 1; i < s2.length; i++) {
			if (s1[0] == s2[i]) {
				dp[0][i] = 1;
			} else {
				dp[0][i] = dp[0][i - 1];
			}
		}
		for (int i = 1; i < s1.length; i++) {
			for (int j = 1; j < s2.length; j++) {
				if (s1[i] == s2[j]) {
					dp[i][j] = dp[i - 1][j - 1] + 1;
				} else {
					dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
				}
			}
		}
		maxLen = dp[s1.length - 1][s2.length - 1];

		return 1000 - maxLen;
	}

	@Override
	public boolean equals(Object obj) {
		return ((Record) obj).getId() == this.id;
	}

	@Override
	public int hashCode() {
		return super.hashCode();// 地址相关的一个值
	}

	public String getInfo() {
		return String.format("[%d]", id);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int[] getUsers() {
		return users;
	}

	public void setUsers(int[] users) {
		this.users = users;
	}

	public boolean isMedoid() {
		return isMedoid;
	}

	public void setMedoid(boolean isMedoid) {
		this.isMedoid = isMedoid;
	}

}