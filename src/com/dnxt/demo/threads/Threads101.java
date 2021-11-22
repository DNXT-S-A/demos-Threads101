package com.dnxt.demo.threads;

import java.util.Random;

public class Threads101 {

	private Boolean cooking = Boolean.TRUE;
	private Boolean eating = Boolean.FALSE;
	private Object cookingLock = new Object();
	private Object eatingLock = new Object();
	private Integer availableFoodItems = 0;

	public static void main(String[] args) {
		Threads101 threads = new Threads101();
	}

	public Threads101() {

		Thread cookThread = new Thread(new Runnable() {

			@Override
			public void run() {
				cookFood();
			}
		});

		cookThread.start();

		for (int i = 0; i < 2; i++) {
			final int threadId = i;
			Thread canibal = new Thread(new Runnable() {

				@Override
				public void run() {
					eat(threadId);
				}
			});

			canibal.start();

//			try {
//				canibal.join();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}

		try {
			cookThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eat(int threadId) {

		try {
			while (true) {
				synchronized (cookingLock) {
					if (cooking || availableFoodItems == 0) {
						System.out.println("C[" + threadId + "]: Will wait for food..");
						cookingLock.wait();
						System.out.println("C[" + threadId + "]: Cook finished. Is there food?..");
					}

					synchronized (eatingLock) {
						if (eating) { // wait for another to finish or food to become
							System.out.println("C[" + threadId + "]: Will wait for others to stop eating..");
							eatingLock.wait();
							// someone has finished. now I can eat
							// eat
						}
						eating = true;
						synchronized (availableFoodItems) {
							if (availableFoodItems > 0) {
								availableFoodItems -= 1;
								System.out.println("C[" + threadId + "]: Eating..");
								Thread.sleep(1000);
								System.out.println("C[" + threadId + "]: BUUURRRPPP!!!");

							} else {
								System.out.println("No food.. :'(");
							}

							eating = false;
							eatingLock.notify();
						}


					}

				}
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public void cookFood() {
		for (int i = 0; i < 100; i++) {

			try {

				synchronized (cookingLock) {
					System.out.println("P: I'm the cook. I'll cook and let everybody know in 5 secs.");
					synchronized (eatingLock) {
						if (eating)
							eatingLock.wait();
					}
					cooking = true;
				}

				Thread.sleep(5000);

				synchronized (cookingLock) {
					synchronized (availableFoodItems) {
						this.availableFoodItems += new Random().nextInt(2) + 1;
					}

					cooking = false;
					System.out.println("P: Finished cooking " + this.availableFoodItems + " food!");
					cookingLock.notifyAll();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
