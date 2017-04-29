package com.sicnu.yudidi.kmedoids.clustering;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

public class KMedoidsClusterer {

	private final static Logger log = Logger.getLogger(KMedoidsClusterer.class);

	private int k; // 分类数量
	private List<DataSetRecord> allRecords; // 所有记录
	// private List<Integer> allRecordIds; // 所有记录的编号
	private Cluster[] clusters; // 所有簇
	private int maxIterations; // 最大迭代次数

	private int[][] distancesMemo; // 距离备忘录

	/**
	 * 
	 * @param k 簇数目
	 * @param allRecords 所有记录
	 * @param maxIterations 最大迭代次数
	 */
	public KMedoidsClusterer(int k, List<DataSetRecord> allRecords, int maxIterations) {
		super();
		this.k = k;
		this.allRecords = allRecords;
		this.maxIterations = maxIterations;
		// 在内部对记录编号
		for (int i = 0; i < allRecords.size(); i++) {
			this.allRecords.get(i).setId(i);
		}
		// 初始化k个簇
		clusters = new Cluster[k];
		for (int i = 0; i < k; i++) {
			clusters[i] = new Cluster(i + "");
		}
		// 初始化距离备忘录
		distancesMemo = new int[allRecords.size()][allRecords.size()];
		for (int i = 0; i < allRecords.size(); i++) {
			for (int j = 0; j < allRecords.size(); j++) {
				distancesMemo[i][j] = i == j ? 0 : -1;
			}
		}
	}

	public Map<String, List<DataSetRecord>> output() {
		Map<String, List<DataSetRecord>> output = new HashMap<>();
		for (Cluster cluster : clusters) {
			String clusterName = cluster.getClusterName();
			for (DataSetRecord record : cluster.getRecordsList()) {
				if (output.get(clusterName) == null) {
					List<DataSetRecord> records = new ArrayList<>();
					records.add(record);
					output.put(clusterName, records);
				} else {
					output.get(clusterName).add(record);
				}
			}
		}
		return output;
	}

	public void saveOutput() {
		for (Cluster cluster : clusters) {
			String clusterName = cluster.getClusterName();
			for (DataSetRecord record : cluster.getRecordsList()) {
			}
		}
	}
	
	/**
	 * 随机选择k个中心记录,然后开始聚类
	 */
	public void randowClustering() {
		log.info("");
		log.info("");
		log.info("*******************************************");
		log.info(String.format("*********[%s]随机聚类器启动***********", new SimpleDateFormat("hh:mm:ss").format(new Date())));
		log.info("*******************************************");
		log.info("");
		log.info("");
		List<DataSetRecord> centralRecords = getRandomCentralRecords();
		generateInitialClusters(centralRecords);
		iterate();
		printClustersInfo(Arrays.asList(clusters));
	}

	public List<DataSetRecord> getRandomCentralRecords() {
		Set<DataSetRecord> centralRecords = new HashSet<>();
		Random random = new Random();
		while (centralRecords.size() != k) {
			int r = random.nextInt(allRecords.size());
			centralRecords.add(allRecords.get(r));
		}
		// log
		for (DataSetRecord record : centralRecords) {
			log.info(String.format("随机选中中心记录id:%d| 中心记录名字:%s| 用户数目:%d", record.getId(), record.getName(), record.getUsers().length));
		}
		return new ArrayList<DataSetRecord>(centralRecords);
	}

	/**
	 * 
	 * @param centralRecords
	 */
	public void clustering(List<DataSetRecord> centralRecords) {
		log.info("");
		log.info("");
		log.info("*******************************************");
		log.info(String.format("*********[%s]聚类器启动***********", new SimpleDateFormat("hh:mm:ss").format(new Date())));
		log.info("*******************************************");
		log.info("");
		log.info("");
		generateInitialClusters(centralRecords);
		iterate();
		printClustersInfo(Arrays.asList(clusters));
	}

	/**
	 * 用非中心点m1替代中心点o1,累计替代代价[s1,..sm],选择替换后产生代价最小且>0,形成新簇. # 思路: 1. 清空新簇 2.
	 * 非中心m1代替中心o1后,重新形成簇,重新形成簇过程中的所有点或付出代价变换簇,或不变换簇. 记录最小代价S,m1,o1. 3.
	 * S<0,可以替代,m1替代o1形成新簇 ; S>0 不可以替代,使用旧簇,迭代结束.
	 */
	public void iterate() {
		int count = 1;
		List<Cluster> tmpClusters = new ArrayList<>(Arrays.asList(clusters));// 存放每次迭代后的临时簇
		List<DataSetRecord> lastCentralRecords = null; // 存放迭代后的中心记录
		DataSetRecord holdOldCenter = null; // 记录被替换的中心记录
		DataSetRecord holdNewCenter = null; // 记录新的中心记录
		int minCost; // 记录一种替换的总代价
		while (true) {
			printClustersInfo(tmpClusters);
			log.info(String.format("========开始第%d次迭代========", count));
			minCost = Integer.MAX_VALUE;
			lastCentralRecords = new ArrayList<>();
			// 记下当前的中心记录集合
			for (Cluster cluster : tmpClusters) {
				lastCentralRecords.add(cluster.getCentralRecord());
			}
			// 所有的替代方案,选择代价最小的,形成新簇
			for (int i = 0; i < allRecords.size(); i++) {
				DataSetRecord record = allRecords.get(i);
				for (int j = 0; j < lastCentralRecords.size(); j++) {
					if (!lastCentralRecords.contains(record)) {
						DataSetRecord newCenter = record;
						DataSetRecord oldCenter = lastCentralRecords.get(j);
						log.debug(String.format("使用非中心记录%s替代中心记录%s", newCenter.getInfo(), oldCenter.getInfo()));
						int allCost = getCostSumOfOneReplace(newCenter, oldCenter, tmpClusters, lastCentralRecords);
						log.debug(String.format("使用记录%s替代记录%s| 所有记录的变动总代价为:%d", newCenter.getInfo(), oldCenter.getInfo(), allCost));
						if (allCost < minCost) {
							minCost = allCost;
							holdNewCenter = newCenter;
							holdOldCenter = oldCenter;
						}
					}
				}
			}
			// 没有合适替代方案,退出,tmpClusters就是结果
			if (minCost >= 0) {
				log.info(String.format("========迭代正常结束| 最小替代代价%d >= 0 |迭代总次数为:%d========",minCost, count));
				break;
			}
			log.info(String.format("使用记录%s替代记录%s的总代价:%d < 0,重新形成簇", holdNewCenter.getId(), holdOldCenter.getId(),minCost));
			// 记录新的中心记录集合
			List<DataSetRecord> newCentralRecords = new ArrayList<>(lastCentralRecords);
			newCentralRecords.remove(holdOldCenter);
			newCentralRecords.add(holdNewCenter);
			// 清空临时簇中的所有记录
			for (int i = 0; i < tmpClusters.size(); i++) {
				tmpClusters.get(i).getRecordsList().clear();
			}
			// 添加中心记录到簇中,并设置簇的中心记录
			for (int i = 0; i < tmpClusters.size(); i++) {
				tmpClusters.get(i).getRecordsList().add(newCentralRecords.get(i));
				tmpClusters.get(i).setCentralRecord(newCentralRecords.get(i));
			}
			// 分配其他记录到临时簇
			for (int i = 0; i < allRecords.size(); i++) {
				DataSetRecord record = allRecords.get(i);
				if (newCentralRecords.contains(record) == false) {// 非最新中心点
					int belongCluster = 0;
					int minDistance = Integer.MAX_VALUE;
					for (int j = 0; j < tmpClusters.size(); j++) {
						int distance2Center = queryDistanceMemo(record, tmpClusters.get(j).getCentralRecord());
						if ( distance2Center < minDistance) {
							//更新最小距离,记录其对应的簇号
							minDistance = distance2Center;
							belongCluster = j;
						}
					}
					tmpClusters.get(belongCluster).addRecord(record);
				}
			}
			log.info(String.format("========结束第%d次迭代========", count++));
			if (count > maxIterations) {
				log.info(String.format("迭代强制结束| 超过最大迭代次%d| 直接退出", maxIterations));
				break;
			}
		}
		clusters = tmpClusters.toArray(new Cluster[tmpClusters.size()]);
	}

	/**
	 * 打印每个簇的记录
	 * 
	 * @param tmpClusters
	 */
	private void printClustersInfo(List<Cluster> tmpClusters) {
		log.info(ClusterConfig.WRAPMID("开始打印所有簇信息"));
		for (Cluster cluster : tmpClusters) {
			log.info(String.format("簇名:%s,中心点:%d,记录个数:%d", cluster.getClusterName(),cluster.getCentralRecord().getId(),cluster.getRecordsList().size()));
			StringBuffer recordIds = new StringBuffer();
			for (DataSetRecord record : cluster.getRecordsList()) {
				recordIds.append(record.getId()+",");
			}
			log.info(String.format("[%s]", recordIds.toString()));
		}
		log.info(ClusterConfig.WRAPMID("结束打印所有簇信息"));
	}

	/**
	 * 计算newCenter替代oldCenter,所有点变动的总代价
	 * 
	 * @param newCenter
	 *            新的中心记录
	 * @param oldCenter
	 *            被替代的中心记录
	 * @param tmpClusters
	 *            临时簇(存放每次迭代结果)
	 * @param lastCentralRecords
	 *            上次迭代结果的中心点集合
	 * @return
	 */
	private int getCostSumOfOneReplace(DataSetRecord newCenter, DataSetRecord oldCenter, List<Cluster> tmpClusters, List<DataSetRecord> lastCentralRecords) {
		List<DataSetRecord> newCentralRecords = new ArrayList<>(lastCentralRecords);
		newCentralRecords.remove(oldCenter);
		newCentralRecords.add(newCenter);
		int costSum = 0;
		// 计算所有点变动前后,到该点变动前后所靠近的中心点的距离的变动值.
		for (Cluster cluster : tmpClusters) {
			for (DataSetRecord record : cluster.getRecordsList()) {
				String[] result = getMinDistance(record, newCentralRecords);
				int newDistance = Integer.valueOf(result[0]); 
				int oldDistance = queryDistanceMemo(record, cluster.getCentralRecord());
				log.debug(String.format("记录%s| 变动后[->%s = %d]| 变动前[->%s = %d]| 变动代价:变动后-变动前 = %d", 
						record.getInfo(), result[1], newDistance, cluster.getCentralRecord().getName(),oldDistance, newDistance - oldDistance));
				costSum += newDistance - oldDistance;
			}
		}
		return costSum;
	}

	/**
	 * 某点到所有中心点的距离的最小的一个
	 * 
	 * @param record
	 *            任意记录
	 * @param centralRecords
	 *            当前的所有中心记录
	 * @return
	 */
	private String[] getMinDistance(DataSetRecord record, List<DataSetRecord> centralRecords) {
		String[] result = new String[2];
	    DataSetRecord nearLyCenter = null;
		int minDistance = Integer.MAX_VALUE;
		for (DataSetRecord centralRecord : centralRecords) {
			if (distancesMemo[record.getId()][centralRecord.getId()] == -1) {
				distancesMemo[record.getId()][centralRecord.getId()] = record.calculateDistance(centralRecord);
			}
			if (distancesMemo[record.getId()][centralRecord.getId()] < minDistance) {
				minDistance = distancesMemo[record.getId()][centralRecord.getId()];
				nearLyCenter = centralRecord;
			}
		}
		result[0] = minDistance+"";
		result[1] = nearLyCenter.getName();
		return result;
	}

	/**
	 * 形成初始簇
	 * 
	 * @param centralRecords
	 */
	private void generateInitialClusters(List<DataSetRecord> centralRecords) {
		// 将记录列表的相应记录标记为中心记录
		for (int i = 0; i < allRecords.size(); i++) {
			if (centralRecords.contains(allRecords.get(i))) {
				allRecords.get(i).setMedoid(true);
			}
		}
		// 为每个簇设置中心记录
		for (int i = 0; i < k; i++) {
			clusters[i].setCentralRecord(centralRecords.get(i));
			// 把中心记录添加到簇
			clusters[i].getRecordsList().add(centralRecords.get(i));
		}
		// 分配记录给簇
		for (int i = 0; i < allRecords.size(); i++) {
			DataSetRecord record = allRecords.get(i);
			if (!record.isMedoid()) {
				int belongCluster = 0;
				int minDistance = Integer.MAX_VALUE;
				for (int j = 0; j < clusters.length; j++) {
					int distance2Center = queryDistanceMemo(record, clusters[j].getCentralRecord());
					if ( distance2Center < minDistance) {
						minDistance = distance2Center;
						belongCluster = j;
					}
				}
				clusters[belongCluster].addRecord(record);
			}
		}
	}

	/**
	 * 查询两个记录的距离
	 * 
	 * @param record1
	 * @param record2
	 * @return
	 */
	public int queryDistanceMemo(DataSetRecord record1, DataSetRecord record2) {
		if (distancesMemo[record1.getId()][record2.getId()] == -1) {
			distancesMemo[record1.getId()][record2.getId()] = record1.calculateDistance(record2);
		}
		return distancesMemo[record1.getId()][record2.getId()];
	}

	public int[][] getDistancesMemo() {
		return distancesMemo;
	}

}