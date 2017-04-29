package com.sicnu.yudidi.kmedoids.clustering;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sicnu.yudidi.utils.crawler.CrawlerConfig;
import com.sicnu.yudidi.utils.file.FileEx;

public class Test {

	public static void main(String[] args) {
		randowCluster();
	}

	public static void randowCluster() {
		// 加载数据集
		List<DataSetRecord> records = loadRecords();
		// 设置聚类器的数据集和簇数目
		KMedoidsClusterer clusterer = new KMedoidsClusterer(4, records, 10);
		// 设置聚类器的初始簇
		clusterer.randowClustering();
		// 打印聚类结果
		Map<String, List<DataSetRecord>> output = clusterer.output();
		for (String clusterName : output.keySet()) {
			List<DataSetRecord> records2 = output.get(clusterName);
			System.out.println(String.format("=====簇:%s; 簇包含%d个记录 ====", clusterName, output.get(clusterName).size()));
			for (int i = 0; i < records2.size(); i++) {
				System.out.println(String.format("记录:%s| 用户数目:%d| 是否中心记录:%s ", records2.get(i).getInfo(), records2.get(i).getUsers().length, records.get(i).isMedoid()));
			}
		}

		int[][] disMemo = clusterer.getDistancesMemo();
		for (int i = 0; i < disMemo.length; i++) {
			for (int j = 0; j < disMemo.length; j++) {
				System.out.print(String.format("[记录%s-->记录%s=%d],", i + 1, j + 1, disMemo[i][j]));
			}
			System.out.println();
		}
	}

	/**
	 * 加载数据集到列表集合
	 * 
	 * @return
	 */
	public static List<DataSetRecord> loadRecords() {
		List<DataSetRecord> records = new ArrayList<>();
		File file = Paths.get(CrawlerConfig.userDir, "output", "dataset.csv").toFile();
		String[] lines = FileEx.readLineByReader(file);
		String recordName = null;
		int[] userIds;
		for (int i = 0; i < lines.length; i++) {
			String[] items = lines[i].split(",");
			recordName = items[0];
			userIds = new int[items.length - 1];
			for (int j = 1; j < items.length; j++) {
				userIds[j - 1] = Integer.valueOf(items[j]);
			}
			records.add(new DataSetRecord(i, recordName, userIds));
		}
		return records;
	}
}
