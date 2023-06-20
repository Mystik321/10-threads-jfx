package ohm.softa.a10.controller;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.kitchen.KitchenHatchImpl;
import ohm.softa.a10.kitchen.workers.Cook;
import ohm.softa.a10.kitchen.workers.Waiter;
import ohm.softa.a10.model.Order;
import ohm.softa.a10.util.NameGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

import static ohm.softa.a10.KitchenHatchConstants.*;

public class MainController implements Initializable {

	private final ProgressReporter progressReporter;
	private final KitchenHatch kitchenHatch;
	private final NameGenerator nameGenerator;

	@FXML
	private ProgressIndicator waitersBusyIndicator;

	@FXML
	private ProgressIndicator cooksBusyIndicator;

	@FXML
	private ProgressBar kitchenHatchProgress;

	@FXML
	private ProgressBar orderQueueProgress;

	public MainController() {
		nameGenerator = new NameGenerator();

		//TODO assign an instance of your implementation of the KitchenHatch interface
		Deque<Order> orders = new LinkedList<>();

		IntStream.range(0, ORDER_COUNT)
			.mapToObj(i -> new Order(nameGenerator.getRandomDish()))
			.forEach(orders::add);

		this.kitchenHatch = new KitchenHatchImpl(KITCHEN_HATCH_SIZE, orders );

		this.progressReporter = new ProgressReporter(kitchenHatch, COOKS_COUNT, WAITERS_COUNT, ORDER_COUNT, KITCHEN_HATCH_SIZE);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		orderQueueProgress.progressProperty().bindBidirectional(this.progressReporter.orderQueueProgressProperty());
		kitchenHatchProgress.progressProperty().bindBidirectional(this.progressReporter.kitchenHatchProgressProperty());
		waitersBusyIndicator.progressProperty().bindBidirectional(this.progressReporter.waitersBusyProperty());
		cooksBusyIndicator.progressProperty().bind(this.progressReporter.cooksBusyProperty());

		/* TODO create the cooks and waiters, pass the kitchen hatch and the reporter instance and start them */
		List<Thread> cooks = new ArrayList<>();
		List<Thread>  waiters = new ArrayList<>();

		for (int n = 0; n < COOKS_COUNT; n++) {
			Thread cook = new Thread(new Cook(nameGenerator.generateName(), progressReporter, kitchenHatch));
			cook.start();
			cooks.add(cook);
		}

		for (int n = 0; n < WAITERS_COUNT; n++) {
			Thread waiter = new Thread(new Waiter(nameGenerator.generateName(), progressReporter, kitchenHatch));
			waiter.start();
			waiters.add(waiter);
		}

	}
}
