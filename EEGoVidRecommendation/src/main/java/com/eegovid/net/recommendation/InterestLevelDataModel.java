package com.eegovid.net.recommendation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;

public class InterestLevelDataModel {
	public static final String EEG_INTEREST_LEVEL_DATASET_FILE = "Data/ratings.csv";

	
	public static DataModel generateDataModel(int dataset) throws Exception {			
		String data = EEG_INTEREST_LEVEL_DATASET_FILE;
		File dataFile = new File(data);
		return new FileDataModel(dataFile);
	}

	public static DataModel getPrefDataByFileFast() throws Exception {		
		String data = EEG_INTEREST_LEVEL_DATASET_FILE;
		FastByIDMap<PreferenceArray> preferences = new FastByIDMap<PreferenceArray>();
		BufferedReader br = new BufferedReader(new FileReader(data));

		List<Preference> list = new ArrayList<Preference>();
		String line = br.readLine();
		long uid_init = Long.parseLong(line.split(",")[0]);
		while (line != null) {
			String[] arr = line.split(",");
			long uid = Long.parseLong(arr[0]);
			if (uid_init == uid) {
				list.add(new GenericPreference(uid, Long.parseLong(arr[1]),
						Float.parseFloat(arr[2])));
			} else {
				preferences.put(uid_init, new GenericUserPreferenceArray(list));
				list = new ArrayList<Preference>();
				list.add(new GenericPreference(uid, Long.parseLong(arr[1]),
						Float.parseFloat(arr[2])));
				uid_init = uid;
			}
			line = br.readLine();

			if (line == null) {
				preferences.put(uid_init, new GenericUserPreferenceArray(list));
			}
		}
		br.close();
		return new GenericDataModel(preferences);
	}

	public static DataModel getNoPrefDataByFile() throws Exception {
		
		String data = EEG_INTEREST_LEVEL_DATASET_FILE;
		return new GenericBooleanPrefDataModel(
				GenericBooleanPrefDataModel.toDataMap(new FileDataModel(
						new File(data))));
	}

	

	public static DataModelBuilder createNoPrefDataModelBuilder()
			throws TasteException {
		return new DataModelBuilder() {
			public DataModel buildDataModel(
					FastByIDMap<PreferenceArray> trainingData) {
				return new GenericBooleanPrefDataModel(
						GenericBooleanPrefDataModel.toDataMap(trainingData));
			}
		};
	}

	public static DataModelBuilder createPrefDataModelBuilder()	throws TasteException {
		return new DataModelBuilder() {
			public DataModel buildDataModel(
					FastByIDMap<PreferenceArray> trainingData) {
				return new GenericDataModel(trainingData);
			}
		};
	}

}
