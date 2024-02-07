package pl.edu.agh.to.thumbnails.server.queue;

import pl.edu.agh.to.thumbnails.server.utils.Error;

public class QueueErrors {
    public static Error Full = new Error("Queue.Add.Full", "Queue is overloaded.");
    public static Error Empty = new Error("Queue.Get.Empty", "Queue is empty, no items to fetch.");
}
