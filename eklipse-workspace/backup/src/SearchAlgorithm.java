import java.util.ArrayList;

public class SearchAlgorithm {

  // Your search algorithm should return a solution in the form of a valid
  // schedule before the deadline given (deadline is given by system time in ms)
  public Schedule solve(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();

    // YOUR CODE HERE

    return solution;
  }

  // This is a very naive baseline scheduling strategy
  // It should be easily beaten by any reasonable strategy
  public Schedule naiveBaseline(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();

    for (int i = 0; i < problem.courses.size(); i++) {
      Course c = problem.courses.get(i);
      boolean scheduled = false;
      for (int j = 0; j < c.timeSlotValues.length; j++) {
        if (scheduled) break;
        if (c.timeSlotValues[j] > 0) {
          for (int k = 0; k < problem.rooms.size(); k++) {
            if (solution.schedule[k][j] < 0) {
              solution.schedule[k][j] = i;
              scheduled = true;
              break;
            }
          }
        }
      }
    }

    return solution;
  }
  
  public Schedule simulatedAnnealing(SchedulingProblem problem, long deadline) {
	  double startTime = System.currentTimeMillis();
      double tMax = 420;
      double tMin = 0;
      Schedule current = problem.getEmptySchedule();
      double currentScore = problem.evaluateSchedule(current);
      Schedule bestSolution = current;
      double bestScore=0;
     

      //C= Cinit
      for (int i = 0; i < problem.courses.size(); i++) {
          Course c = problem.courses.get(i);
          boolean scheduled = false;
          for (int j = 0; j < c.timeSlotValues.length; j++) {
              if (scheduled)
                  break;
              if (c.timeSlotValues[j] > 0) {
                  for (int k = 0; k < problem.rooms.size(); k++) {
                      if (current.schedule[k][j] < 0) {
                          current.schedule[k][j] = i;
                          scheduled = true;
                          break;
                      }
                  }
              }
          }
      }
      
      double timeLeft = deadline - (System.currentTimeMillis() - startTime);
      
      for(double T = tMax ; T<=tMin; T -= 0.0001 ) {
    	  if(timeLeft<=0)
    		  break;
    	  
    	  currentScore =problem.evaluateSchedule(current); //evaluate current position (Ec=E(c)
    	  /* generating successors */
          Schedule next = current.nextArrangement(problem);
          double nextScore = problem.evaluateSchedule(next); //N = next(c)
          double delta = nextScore - currentScore;
          if(delta >0) {
	          bestScore = nextScore; //probably useless
	          bestSolution = next;
	          current = next;
          }
          else if(Math.exp(delta/T) > Math.random()){
        	  current=next;
          }
		  timeLeft = deadline - System.currentTimeMillis();
      }//for loop

	return bestSolution;
  }
  
  public Schedule Backtracking(SchedulingProblem problem, long deadline) {
	  Schedule current = problem.getEmptySchedule();
	  ArrayList<Course> courses = new ArrayList<Course>(problem.courses);
      current = BacktrackingRec(problem, current, courses);
	  
	  return current;
  }
  
  private Schedule BacktrackingRec(SchedulingProblem problem, Schedule current, ArrayList<Course> notVisited) {
	  if(notVisited.size() == 0)
		  return current;
	  Course MRVCourse = MRV(notVisited);
	  if(MRVCourse == null) return current;
	  
	  notVisited.remove(MRVCourse);
	  int mostConflictsIndex = LCV(MRVCourse, problem.courses);
	  for (int i = 0; i < problem.rooms.size(); i++) {
		  if (current.schedule[i][mostConflictsIndex] < 0) { //WHAT IS THIS LINE DOING 
			  if(MRVCourse.enrolledStudents <= problem.rooms.get(i).capacity) { 				 //If the course with minimum remaining
				  current.schedule[i][mostConflictsIndex] = problem.courses.indexOf(MRVCourse);  //values has less than or equal number of
				  break;																		 //students as the room's capacity, then
			  }																					 //set the MRV course to the schedule of
		  }																						 //the current solution
		  																					 
	  }
	  MRVCourse.set = true;
	  current = BacktrackingRec(problem, current, notVisited);
	  if(!complete(problem)) {
		  MRVCourse.timeSlotValues[mostConflictsIndex] = 0;
		  notVisited.add(MRVCourse);
	  }
	  return current;
	  
  }
  
  private int LCV(Course course, ArrayList<Course> courses) { //rename course
	  int[] conflicts = new int[course.timeSlotValues.length];
	  for(Course current: courses) {
		  for (int i = 0; i < conflicts.length; i++) {
			if(current.timeSlotValues[i]> 0 && course.timeSlotValues[i] > 0)
				conflicts[i]++;
		}
	  }
	  int mostConflictsIndex = 0;
	  for (int i = 0; i < conflicts.length; i++) {
		if(conflicts[i] > mostConflictsIndex)
			mostConflictsIndex = i;
	}
	  return mostConflictsIndex;
  }
  
  public Course MRV(ArrayList<Course> courses){
	  Course MRVCourse = null;
	  int minLegalValues = Integer.MAX_VALUE;
	  int currentLegalValues;
	  for(Course current: courses) {
		  currentLegalValues = 0;
		  for(int i = 0; i < current.timeSlotValues.length; i++) {
			  if(current.timeSlotValues[i] > 0)
				  currentLegalValues ++;
		  }
		  
		  if(currentLegalValues < minLegalValues) {
			  minLegalValues = currentLegalValues;
			  MRVCourse = current;
		  }
		  
		  if(currentLegalValues == minLegalValues)
			  MRVCourse = tieBreak(current, MRVCourse);  
		  }
	  
	  return MRVCourse;
  }

  
  private Course tieBreak(Course current, Course MRVCourse) {
	  if(current.value > MRVCourse.value) // Greedy pick
		  MRVCourse = current;
	  else if(current.value == MRVCourse.value) { //if equal, then random pick
		  if(Math.random() >= .5)
			  return current;
	  }

	  return MRVCourse;
  }

  private boolean complete(SchedulingProblem problem) {
	  for(Course course: problem.courses) {
		  if(!course.set)
			  return false;
	  }
	  return true;
  }
}
