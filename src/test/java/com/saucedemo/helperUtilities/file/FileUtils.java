package com.saucedemo.helperUtilities.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {

    public static void deleteFiles(String dirPath, String ext) {
        System.out.println("clearing down all the downloaded reports");
        File dir = new File(dirPath);
        if (!dir.exists()) return;
        File[] fList = dir.listFiles();
        assert fList != null;
        for (File f : fList) {
            if (f.getName().endsWith(ext)) f.delete();
        }
    }

  /*  public static String getReportFile(String fileExtension) {
    File frontEndReportCsv = new File(AdminTestData.REPORTING_DOWNLOAD_FILEPATH);
    String[] fileList = frontEndReportCsv.list();
    assert fileList != null;
    String csvFile =
        Arrays.stream(fileList).filter(attr -> attr.contains(fileExtension)).findFirst().get();
    return csvFile;
  }*/

    public static void compareCsvFiles() throws Exception {
    /* File masterFlexFrontEndFile =
        new File(AdminTestData.REPORTING_DOWNLOAD_FILEPATH + getReportFile(".csv"));
    File masterFlexBackend =
        new File(AdminTestData.REPORTING_DOWNLOAD_FILEPATH + "/masterFlex-output.csv");
    CsvComparisonSource<File> source =
        CsvComparisonSource.create(masterFlexFrontEndFile, masterFlexBackend);
    CsvComparisonOptions options =
        CsvComparisonOptions.builder()
            .setColumns(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
                23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43,
                44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64,
                65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85,
                86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104,
                105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121,
                122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138,
                139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155,
                156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172,
                173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189,
                190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206,
                207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223,
                224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240,
                241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257,
                258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274,
                275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291,
                292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308,
                309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325,
                326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342,
                343)
            .setIdentityColumn(0)
            .build();
    CsvComparisonResult result = new CsvComparator(source, options).compare();
    Boolean reportResult = result.hasDiff();
    Assert.assertEquals(true, reportResult);*/
    }

    public static Map<String, String> getMapFromCSV(final String filePath, String headerKey) throws IOException {
        Stream<String> lines = Files.lines(Paths.get(filePath));
        Map<String, String> resultMap = lines.map(line -> line.split(",")).collect(Collectors.toMap(line -> line[0], line -> line[1]));
        lines.close();
        resultMap.remove(headerKey);
        return resultMap;
    }

  /*public static Map<String, String> readEoReportCsv() throws IOException {
    BufferedReader br =
        new BufferedReader(
            new FileReader(AdminTestData.REPORTING_DOWNLOAD_FILEPATH + getReportFile(".csv")));
    Map<String, String> map = new HashMap<>();
    String line;
    int count = 0;
    while (count < 67) {
      while ((line = br.readLine()) != null) {
        String[] cols = line.split(",");
        if (!cols[2].equals("0")) {
          if (cols[1].equalsIgnoreCase("24-44 years")) {
            map.put("AgeBand_24-44", cols[2]);
          } else if (cols[1].equalsIgnoreCase("45-59 years")) {
            map.put("AgeBand_45-59", cols[2]);
          } else if (cols[1].equalsIgnoreCase("60-74 years")) {
            map.put("AgeBand_60-74", cols[2]);
          } else if (cols[1].equalsIgnoreCase("75+ years")) {
            map.put("AgeBand_75+", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Prefer not to say")) {
            map.put("AgeBand_Prefer not to say", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Under 24 years")) {
            map.put("AgeBand_Under 24 years", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Learning Disability/Difficulty")) {
            map.put("Disability_Learning Disability/Difficulty", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Long Standing Illness")) {
            map.put("Disability_Long Standing Illness", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Mental Health Condition")) {
            map.put("Disability_Mental Health Condition", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Other")) {
            map.put("Disability_Other", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Physical Impairment")) {
            map.put("Disability_Physical Impairment", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Sensory Impairment")) {
            map.put("Disability_Sensory Impairment", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Any other ethnic group")) {
            map.put("Ethnicity_Any other ethnic group", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Any other white background")) {
            map.put("Ethnicity_Any other white background", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Arab")) {
            map.put("Ethnicity_Arab", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Asian/Asian British: Bangladeshi")) {
            map.put("Ethnicity_Asian/Asian British: Bangladeshi", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Asian/Asian British: Chinese")) {
            map.put("Ethnicity_Asian/Asian British: Chinese", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Asian/Asian British: Indian")) {
            map.put("Ethnicity_Asian/Asian British: Indian", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Asian/Asian British: Other")) {
            map.put("Ethnicity_Asian/Asian British: Other", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Asian/Asian British: Pakistani")) {
            map.put("Ethnicity_Asian/Asian British: Pakistani", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Black/Black British: African")) {
            map.put("Ethnicity_Black/Black British: African", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Black/Black British: Caribbean")) {
            map.put("Ethnicity_Black/Black British: Caribbean", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Black/Black British: Other")) {
            map.put("Ethnicity_Black/Black British: Other", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Mixed: Other")) {
            map.put("Ethnicity_Mixed: Other", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Mixed: White and Asian")) {
            map.put("Ethnicity_Mixed: White and Asian", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Mixed: White and Black African")) {
            map.put("Ethnicity_Mixed: White and Black African", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Mixed: White and Black Caribbean")) {
            map.put("Ethnicity_Mixed: White and Black Caribbean", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Prefer not to say")) {
            map.put("Ethnicity_Prefer not to say", cols[2]);
          } else if (cols[1].equalsIgnoreCase("White: Irish")) {
            map.put("Ethnicity_White: Irish", cols[2]);
          } else if (cols[1].equalsIgnoreCase("White: Traveller")) {
            map.put("Ethnicity_White: Traveller", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Female")) {
            map.put("Gender_Female", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Male")) {
            map.put("Gender_Male", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Other")) {
            map.put("Gender_Other", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Prefer not to say")) {
            map.put("Gender_Prefer not to say", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Civil Partnership")) {
            map.put("Marital_Status_Civil Partnership", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Divorced")) {
            map.put("Marital_Status_Divorced", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Legally Separated")) {
            map.put("Marital_Status_Legally Separated", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Married")) {
            map.put("Marital_Status_Married", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Prefer not to say")) {
            map.put("Marital_Status_Prefer not to say", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Single")) {
            map.put("Marital_Status_Single", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Widowed")) {
            map.put("Marital_Status_Widowed", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Any other religion")) {
            map.put("Religion_Any other religion", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Atheism/no religion")) {
            map.put("Religion_Atheism/no religion", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Buddhism")) {
            map.put("Religion_Buddhism", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Hinduism")) {
            map.put("Religion_Hinduism", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Islam")) {
            map.put("Religion_Islam", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Jainism")) {
            map.put("Religion_Jainism", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Judaism")) {
            map.put("Religion_Judaism", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Prefer not to say")) {
            map.put("Religion_Prefer not to say", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Sikhism")) {
            map.put("Religion_Sikhism", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Bisexual")) {
            map.put("Sexual_Orientation_Bisexual", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Gay/Lesbian")) {
            map.put("Sexual_Orientation_Gay/Lesbian", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Hetrosexual/Straight")) {
            map.put("Sexual_Orientation_Hetrosexual/Straight", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Other sexual orientation not listed")) {
            map.put("Sexual_Orientation_Other sexual orientation not listed", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Prefer not to say")) {
            map.put("Sexual_Orientation_Prefer not to say", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Undecided")) {
            map.put("Sexual_Orientation_Undecided", cols[2]);
          } else if (cols[1].equalsIgnoreCase("Total")) {
            map.put("Total", cols[2]);
          }
        }
        count++;
      }
    }
    br.close();
    return map;
  }*/

    public static <K, V> K getKey(Map<K, V> map, V value) {
        for (Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

  /*  public static void deleteCSVFiles() {
    File dir = new File(AdminTestData.APPLICATIONS_DOWNLOAD_FILEPATH);
    File[] dirContents = dir.listFiles();
    assert dirContents != null;
    for (File f : dirContents) {
      for (int i = 0; i < dirContents.length; i++) {
        if (f.getName().contains(AdminTestData.APPLICATION_CONTACT_DETAILS_DOWNLOAD_FILENAME))
          f.delete();
      }
    }
  }*/

    public static void writeByteArrayToFile(File file, byte[] screenshot) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(screenshot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
