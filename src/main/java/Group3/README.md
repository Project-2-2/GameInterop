# Group 3 Agents:

Our agents are **Guard2/Guard2.java** and **Intruder/Intruder.java**. The agents are automatically created using the AgentsFactory.java or IAgentsFactoryGroup3.java. If you want to initialize them separately, you need to create the objects in your own AgentsFactory.

Feel free to contact our group anytime if you have any questions or experience any issues.

**Let the games begin!!!**

# Known bugs and problems:

* If the intruder capture distance is too small (< 1), it is hard for the guards to catch the intruders. 
* If the maximum move distance is too small (< 0.4), the program gets very slow. This is because of the high amount of discrete vertices in a graph map, that are created for each possible move.
