package irctc.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import irctc.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAINS_DB_PATH = "app/src/main/java/irctc/booking/localDb/trains.json";

    public TrainService() throws IOException{
        loadAllTrains();
    }

    private void loadAllTrains() throws IOException {
        File trains = new File(TRAINS_DB_PATH);
        trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});

    }

    public List<Train> searchTrains(String source, String dest) throws IOException {
        return trainList.stream().filter(train -> validTrain(train, source, dest))
                .collect(Collectors.toList());
    }

    public void addTrain(Train newTrain) {
        // Check if a train with same Id already exists
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId().equals(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            // If a train exists, update it instead of adding a new one
            updateTrain(newTrain);
        } else {
            // Add new train to the list
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) {
        // Find index of the train with same train id
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            addTrain(updatedTrain);
        }
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();

        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destIndex = stationOrder.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destIndex != -1 && sourceIndex < destIndex;
    }

    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAINS_DB_PATH), trainList);
        } catch (IOException ex) {
            System.out.println("Caught exception while saving new train into the train list.");
        }
    }

}
