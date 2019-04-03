public class Schedule {
  int[][] schedule;

  Schedule(int nRooms, int nTimeSlots) {
    schedule = new int[nRooms][nTimeSlots];
  }
  
  public Schedule nextArrangement(SchedulingProblem problem){
      Schedule copy = clone();
      int randomRoom1 = problem.random.nextInt(schedule.length); //get a random number between 0 and schedule.length
      int course1 = problem.random.nextInt(schedule[randomRoom1].length);  //get a random number between 0 and the length of randomRoom1.length
      
      int randomRoom2 = -1; //can change to null declaration
      do {
          randomRoom2 = problem.random.nextInt(schedule.length);
      } while(randomRoom1 == randomRoom2);
      int course2 = problem.random.nextInt(schedule[randomRoom2].length);
      
      // Swap their times
      int temp = schedule[randomRoom1][course1];
      
      copy.schedule[randomRoom1][course1] = schedule[randomRoom2][course2];
      copy.schedule[randomRoom2][course2] = temp;
      
      return copy;
  }
  
  
  @Override
  public Schedule clone() {
    Schedule copy = new Schedule(schedule.length, schedule[0].length);
    for(int i = 0; i < schedule.length; i++)
        for(int j = 0; j < schedule[i].length; j++)
            copy.schedule[i][j] = schedule[i][j];
    
    return copy;
}
}
