package racingcar.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import racingcar.RandomNumberGenerator;
import racingcar.dto.CarPositionDto;
import racingcar.dto.WinnerDto;
import racingcar.view.InputView;
import racingcar.view.OutputView;

public class RacingGameController {

    private final InputView inputView;
    private final OutputView outputView;

    public RacingGameController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        Cars cars = createCars();
        int count = createCount();
        outputView.printRunMessage();
        for (int i = 0; i < count; i++) {
            cars.play(new RandomNumberGenerator());
            outputView.printCarsPosition(getCarPosition(cars));
        }
        Cars winners = cars.getWinners();
        outputView.printWinnerNames(getWinnerPositionDto(winners));
    }

    private Cars createCars() {
        String namesOfParticipatingCars = inputView.sendNamesOfParticipatingCars();
        List<Car> cars = Arrays.stream(namesOfParticipatingCars.split(","))
                .map(Car::new)
                .collect(Collectors.toList());
        return new Cars(cars);
    }

    private int createCount() {
        return convertInputToInt(inputView.sendCountOfMoves());
    }

    private int convertInputToInt(String input) {
        if (isNotNumber(input)) {
            throw new IllegalArgumentException("숫자를 입력해주세요. 입력값 : " + input);
        }
        return Integer.parseInt(input);
    }

    private boolean isNotNumber(String input) {
        return !input.chars().allMatch(Character::isDigit);
    }

    private List<CarPositionDto> getCarPosition(Cars cars) {
        return cars.getCars()
                .stream()
                .map(CarPositionDto::of)
                .collect(Collectors.toList());
    }

    private List<WinnerDto> getWinnerPositionDto(Cars cars) {
        return cars.getCars()
                .stream()
                .map(car -> new WinnerDto(car.getName()))
                .collect(Collectors.toList());
    }
}
