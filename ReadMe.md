# PriceBasket
A Scala command-line application to calculate the total price of a shopping basket with discounts applied.

---

## **How to Run**

```
sbt clean compile
```

### **Run using sbt**
From the project root, run:
```bash
sbt "run Apples Milk Bread"
```

## Package and Run as JAR
```
sbt clean assembly
java -jar target/scala-2.13/pricebasket-assembly-0.1.0-SNAPSHOT.jar "Apples Milk Bread"
```

### **Run using Shell Alias**
```
alias PriceBasket='java -jar target/scala-3.3.6/PriceBasket-assembly-0.1.0-SNAPSHOT.jar'
PriceBasket Apples Milk Bread
```
