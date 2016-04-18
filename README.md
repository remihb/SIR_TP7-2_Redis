# Redis

Redis est une bdd de type **clef-valeur**, c'est une sorte de hashmap. Elle est monothread (il me semble) et sa force réside dans sa vitesse de lecture/écriture.  

Nous utilisons ici Jedis, un client java pour Redis

```java
Jedis jedis = new Jedis("localhost");
//	Delete all the keys of all the existing databases
jedis.flushDB();
```
Il tourne ici en local, le serveur est lancé localement, et je flush toutes les clés de la base courante afin d'avoir le même déroulement que décrit ci-dessous (pas de clé déjà existantes).

Il existe différent type de données de stockage avec Redis.

### Simple

Le type le plus simple est d'associer à une *clé* une *valeur* de type **string** ou **int**

##### String
```java
Jedis jedis = new Jedis("localhost");
jedis.set("foo", "bar");
String value = jedis.get("foo");
System.out.println(value);  
```

```bash
> bar
```

#### Int

On peut voir que si la clé n'existe pas, la méthode **incr** initialise la valeur à 0 avant d'effectuer l'opération.

```java
Jedis jedis = new Jedis("localhost");
System.out.println(jedis.get("counter"));
jedis.incr("counter");
System.out.println(jedis.get("counter"));
```

```bash
> null
> 1
```


### Sets

Il s'agit d'une collection d'objets non ordonnés, on peut voir que les duplications ne sont pas autorisées.

```java
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
```

```bash
> Languages: [C#, Java, Python]
> Languages: [C#, Java, Ruby, Python]
```

Il existe d'autres types de stockages comme les tableaux associatifs ou les listes. Il existe également de nombreuses méthodes pour travailler sur les éléments stockés.

#### Expire/TTL

La méthode **expire** permet de donner une *durée de vie* à une clé donnée.
On peut récupérer la durée de vie restante avec la méthode **ttl**.

Ici on a donné 15 secondes de vie à la clé *cacheKey*, et on peut voir qu'au bout de 15 secondes, la valeur associée à *cacheKey* est passée de **cached value** a **null**.

```java
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
```

```bash
> TTL:15
> TTL:14
> Cached Value:cached value
> Expired Key:null
```
