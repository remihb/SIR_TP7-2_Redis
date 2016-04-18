package fr.istic.sir.tp7_2;

import redis.clients.jedis.Jedis;

/**
 * Hello world!
 *
 */
public class App 
{
	public static void main( String[] args ) throws InterruptedException
	{
		Jedis jedis = new Jedis("localhost");
		//	Delete all the keys of the currently selected database
		jedis.flushDB();
		System.out.println("==========================ex1=======================");
		ex1(jedis);
		System.out.println("\n==========================ex2=======================");
		ex2(jedis);
		System.out.println("\n==========================ex3=======================");
		ex3(jedis);
		System.out.println("\n==========================ex4=======================");
		ex4(jedis);
	}
	
	public static void ex1(Jedis jedis){
		jedis.set("foo", "bar");
		String value = jedis.get("foo");
		System.err.println(value);    
	}
	
	public static void ex2(Jedis jedis) {
        System.out.println(jedis.get("counter"));
        jedis.incr("counter");
        System.out.println(jedis.get("counter"));
    }

	public static void ex3(Jedis jedis) throws InterruptedException {
        String cacheKey = "cachekey";
        // adding a new key
        jedis.set(cacheKey, "cached value");
        // setting the TTL in seconds
        jedis.expire(cacheKey, 15);
        // Getting the remaining ttl
        System.out.println("TTL:" + jedis.ttl(cacheKey));
        Thread.sleep(1000);
        System.out.println("TTL:" + jedis.ttl(cacheKey));
        // Getting the cache value
        System.out.println("Cached Value:" + jedis.get(cacheKey));

        // Wait for the TTL finishs
        Thread.sleep(15000);

        // trying to get the expired key
        System.out.println("Expired Key:" + jedis.get(cacheKey));
    }
	
	public static void ex4(Jedis jedis) {
        String cacheKey = "languages";
        // Adding a set as value

        jedis.sadd(cacheKey, "Java");
        jedis.sadd(cacheKey, "C#");
        jedis.sadd(cacheKey, "Python");// SADD

        // Getting all values in the set: SMEMBERS
        System.out.println("Languages: " + jedis.smembers(cacheKey));
        // Adding new values
        jedis.sadd(cacheKey, "Java");
        jedis.sadd(cacheKey, "Ruby");
        // Getting the values... it doesn't allow duplicates
        System.out.println("Languages: " + jedis.smembers(cacheKey));

    }


}
