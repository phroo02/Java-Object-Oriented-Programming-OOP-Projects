package mountainhuts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Class {@code Region} represents the main facade
 * class for the mountains hut system.
 * 
 * It allows defining and retrieving information about
 * municipalities and mountain huts.
 *
 */
public class Region {

	/**
	 * Create a region with the given name.
	 * 
	 * @param name
	 *            the name of the region
	 */
	private String name;
	private List<String> altitudes=new ArrayList<>();
	private Map<String,Municipality> municipalities=new HashMap<>();
	private Map<String,MountainHut> mountainHuts=new HashMap<>();

	public Region(String name) {
		this.name=name;
	}

	/**
	 * Return the name of the region.
	 * 
	 * @return the name of the region
	 */
	public String getName() {
		return name;
	}

	/**
	 * Create the ranges given their textual representation in the format
	 * "[minValue]-[maxValue]".
	 * 
	 * @param ranges
	 *            an array of textual ranges
	 */
	public void setAltitudeRanges(String... ranges) {
		for(String s :ranges){
			altitudes.add(s);
		}
	}

	/**
	 * Return the textual representation in the format "[minValue]-[maxValue]" of
	 * the range including the given altitude or return the default range "0-INF".
	 * 
	 * @param altitude
	 *            the geographical altitude
	 * @return a string representing the range
	 */
	public String getAltitudeRange(Integer altitude) {
		 return altitudes.stream()
            .filter(range -> {
                String[] parts = range.split("-");
                int min = Integer.parseInt(parts[0]);
                int max = Integer.parseInt(parts[1]);
                return altitude >= min && altitude <= max;
            })
            .findFirst()
            .orElse("0-INF");
	}

	/**
	 * Return all the municipalities available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of municipalities
	 */
	public Collection<Municipality> getMunicipalities() {
		return municipalities.values();
	}

	/**
	 * Return all the mountain huts available.
	 * 
	 * The returned collection is unmodifiable
	 * 
	 * @return a collection of mountain huts
	 */
	public Collection<MountainHut> getMountainHuts() {
		return mountainHuts.values()
		
	
		
		;
	}

	/**
	 * Create a new municipality if it is not already available or find it.
	 * Duplicates must be detected by comparing the municipality names.
	 * 
	 * @param name
	 *            the municipality name
	 * @param province
	 *            the municipality province
	 * @param altitude
	 *            the municipality altitude
	 * @return the municipality
	 */
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {
		Municipality m=municipalities.get(name);
		if(m==null){
			m=new Municipality(name,province,altitude);
			municipalities.put(name, m);
			return m;
		}
		return m;
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return the mountain hut
	 */

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 * 
	 * @param name
	 *            the mountain hut name
	 * @param altitude
	 *            the mountain hut altitude
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return a mountain hut
	 */
    public MountainHut createOrGetMountainHut(String name,String category,int bedsNumber,Municipality municipality){
		MountainHut n=mountainHuts.get(name);
		if(n==null){
			n=new MountainHut(name,null,category,bedsNumber,municipality);
			mountainHuts.put(name, n);
			return n;
		}
		return n;
	}
	public MountainHut createOrGetMountainHut(String name,Integer altitude,String category,int bedsNumber,Municipality municipality){
		MountainHut n=mountainHuts.get(name);
		if(n==null){
			n=new MountainHut(name,altitude,category,bedsNumber,municipality);
			mountainHuts.put(name, n);
			return n;
		}
		return n;
	}
	/**
	 * Creates a new region and loads its data from a file.
	 * 
	 * The file must be a CSV file and it must contain the following fields:
	 * <ul>
	 * <li>{@code "Province"},
	 * <li>{@code "Municipality"},
	 * <li>{@code "MunicipalityAltitude"},
	 * <li>{@code "Name"},
	 * <li>{@code "Altitude"},
	 * <li>{@code "Category"},
	 * <li>{@code "BedsNumber"}
	 * </ul>
	 * 
	 * The fields are separated by a semicolon (';'). The field {@code "Altitude"}
	 * may be empty.
	 * 
	 * @param name
	 *            the name of the region
	 * @param file
	 *            the path of the file
	 */
	public static Region fromFile(String name, String file) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(file);
		 Region region = new Region(name);

    List<String> lines = readData(file);
    if (lines.isEmpty()) return region;

    // Skip header
    lines.stream().skip(1).forEach(line -> {
        String[] fields = line.split(";");

        String province = fields[0].trim();
        String municipalityName = fields[1].trim();
        Integer municipalityAltitude = Integer.parseInt(fields[2].trim());
        String hutName = fields[3].trim();
        String altitudeStr = fields[4].trim();
        String category = fields[5].trim();
        Integer bedsNumber = Integer.parseInt(fields[6].trim());

        // Create or get Municipality
        Municipality municipality = region.createOrGetMunicipality(municipalityName, province, municipalityAltitude);

        // Handle nullable altitude
        Integer altitude = altitudeStr.isEmpty() ? null : Integer.parseInt(altitudeStr);

        // Create or get MountainHut
        region.createOrGetMountainHut(hutName,altitude,category, bedsNumber, municipality);
    });

    return region;
		
    
	}

	/**
	 * Reads the lines of a text file.
	 *
	 * @param file path of the file
	 * @return a list with one element per line
	 */
	public static List<String> readData(String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			return in.lines().toList();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Count the number of municipalities with at least a mountain hut per each
	 * province.
	 * 
	 * @return a map with the province as key and the number of municipalities as
	 *         value
	 */
	public Map<String, Long> countMunicipalitiesPerProvince() {
		 return municipalities.values().stream()
        .collect(Collectors.groupingBy(
            Municipality::getProvince, // group by province
            Collectors.counting()      // count how many per group
        ));
	}

	/**
	 * Count the number of mountain huts per each municipality within each province.
	 * 
	 * @return a map with the province as key and, as value, a map with the
	 *         municipality as key and the number of mountain huts as value
	 */
	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
		 return mountainHuts.values().stream()
        .collect(Collectors.groupingBy(
            hut -> hut.getMunicipality().getProvince(),
            Collectors.groupingBy(
                hut -> hut.getMunicipality().getName(),
                Collectors.counting()
            )
        ));
	}

	/**
	 * Count the number of mountain huts per altitude range. If the altitude of the
	 * mountain hut is not available, use the altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the number of mountain huts
	 *         as value
	 */
	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		return mountainHuts.values().stream()
        .map(hut -> {
            int altitude = hut.getAltitude()
                              .orElse(hut.getMunicipality().getAltitude());
            return getAltitudeRange(altitude);
        })
        .collect(Collectors.groupingBy(
            range -> range,
            Collectors.counting()
        ));
	}

	/**
	 * Compute the total number of beds available in the mountain huts per each
	 * province.
	 * 
	 * @return a map with the province as key and the total number of beds as value
	 */
	public Map<String, Integer> totalBedsNumberPerProvince() {
		 return mountainHuts.values().stream()
        .collect(Collectors.groupingBy(
            hut -> hut.getMunicipality().getProvince(),
            Collectors.summingInt(MountainHut::getBedsNumber)
        ));
	}

	/**
	 * Compute the maximum number of beds available in a single mountain hut per
	 * altitude range. If the altitude of the mountain hut is not available, use the
	 * altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the maximum number of beds
	 *         as value
	 */
	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		 return mountainHuts.values().stream()
        .collect(Collectors.groupingBy(
            hut -> {
                int effectiveAltitude = hut.getAltitude()
                                            .orElse(hut.getMunicipality().getAltitude());
                return getAltitudeRange(effectiveAltitude);
            },
            Collectors.mapping(
                MountainHut::getBedsNumber,
                Collectors.maxBy(Integer::compareTo)
            )
        ));
	}

	/**
	 * Compute the municipality names per number of mountain huts in a municipality.
	 * The lists of municipality names must be in alphabetical order.
	 * 
	 * @return a map with the number of mountain huts in a municipality as key and a
	 *         list of municipality names as value
	 */
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {
		 return mountainHuts.values().stream()
        .collect(Collectors.groupingBy(
            hut -> hut.getMunicipality().getName(),
            Collectors.counting()
        ))
        .entrySet().stream()
        .collect(Collectors.groupingBy(
            Map.Entry::getValue,
            Collectors.mapping(
                Map.Entry::getKey,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    list -> {
                        list.sort(String::compareTo);
                        return list;
                    }
                )
            )
        ));
	}

}
