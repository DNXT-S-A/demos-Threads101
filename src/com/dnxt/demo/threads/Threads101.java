package com.dnxt.demo.threads;

public class Threads101 {

	private Object lock = new Object();

	public static void main(String[] args) {
		Threads101 threads = new Threads101();
	}

	public Threads101() {
		for (int i = 0; i < 10; i++) {
			Thread consumerThread = new Thread(new Runnable() {

				@Override
				public void run() {
					consumer();

				}
			});

			try {
				consumerThread.join();
				consumerThread.start();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		Thread producerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				producer();

			}
		});

		try {
			producerThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		producerThread.start();
	}

	public void consumer() {
		synchronized (lock) {
			try {
				System.out.println("C: Will wait to get awaken..");
				lock.wait();
				System.out.println("C: I got awaken!!!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void producer() {
		synchronized (lock) {
			System.out.println("P: I'm the producer. I'll awake the consumer in 5 secs.");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				System.out.println("P: Waking consumer now!");
				lock.notifyAll();
			}
		}
	}

}
