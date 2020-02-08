package it.guaraldi.to_dotaskmanager.ui.edittask;

import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import it.guaraldi.to_dotaskmanager.data.TasksDataSource;
import it.guaraldi.to_dotaskmanager.data.TasksRepository;
import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.ui.base.BasePresenter;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.PersonalizedInstanceState;
import it.guaraldi.to_dotaskmanager.ui.edittask.personalized.child.ChildPersonalizedInstanceState;
import it.guaraldi.to_dotaskmanager.utils.DateUtils;

import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.LOAD_CATEGORY;
import static it.guaraldi.to_dotaskmanager.utils.ActivityUtils.REMOVE_CATEGORY;

public class EditTaskPresenter extends BasePresenter<EditTaskContract.View> implements EditTaskContract.Presenter {
    private final TasksRepository mRepository;
    private int mCurrentTaskId;
    private int mSizeTableTasks;
    private  static final long dayDuration = 1000 * 60 * 60 * 24;
    private static final long weekDuration = 1000 * 60 * 60 * 24 * 7;

    private static final String TAG = "EditTaskPresenter";

    @Inject
    public EditTaskPresenter (TasksRepository repository){
        mRepository = repository;
    }

    @Override
    public void saveNewTask(String title, String email, boolean allDay, int priority, String category, String dateStart,
                            String timeStart, String dateEnd, String timeEnd, String reply, boolean isReply, String description,
                            String color, PersonalizedInstanceState personalizedInstance, ChildPersonalizedInstanceState childPersonalizedInstance) {


        String start = allDay ? Long.toString(DateUtils.stringToLongAllDayDate(dateStart)):
                                Long.toString(DateUtils.stringToLongCompleteDate(dateStart,timeStart));
        String end = allDay? Long.toString(DateUtils.stringToLongAllDayDate(dateEnd)):
                             Long.toString(DateUtils.stringToLongCompleteDate(dateEnd,timeEnd));

        if(isReply){
            Log.d(TAG, "saveNewTask: IS REPLY");
            if(personalizedInstance != null){
                Log.d(TAG, "saveNewTask: personalized NOT NULL");
                if(childPersonalizedInstance != null){
                    Log.d(TAG, "saveNewTask: childPersionalized NOT NULL");
                    String groupId = UUID.randomUUID().toString();
                    Task task = new Task(Integer.toString(mCurrentTaskId), title, email, allDay,groupId, priority,
                            category,"PENDING", start,end,"0,0","0,0",description,color);

                    Log.d(TAG, "saveNewTask: getMonthSpinnerId="+childPersonalizedInstance.getMonthSpinnerId());
                    Log.d(TAG, "saveNewTask: getMonthSpinner="+childPersonalizedInstance.getMonthSpinner());



                    String endDayTv = childPersonalizedInstance.getEndDayTv();
                    boolean isAfterBtn = childPersonalizedInstance.isAfterBtn();
                    boolean isTheDayBtn = childPersonalizedInstance.isTheDayBtn();
                    int repeatEveryNumber = personalizedInstance.getNumberPeriodRepetition();
                    String repeatEveryPeriod = personalizedInstance.getPeriodNameSelection();
                        switch (repeatEveryPeriod){
                            case "day":
                            case "days":
                                if(!isAfterBtn  && !isTheDayBtn) {
                                  //TODO INIFINITO
                                }
                                if(isTheDayBtn) {

                                    Calendar lastDay = DateUtils.stringToCalendarLastDay(endDayTv);
                                    calculateTasksForEveryDay2(task,lastDay,repeatEveryNumber,groupId);
                                }
                                if(isAfterBtn) {
                                    int occurence = Integer.parseInt(childPersonalizedInstance.getNumberOccorenceEdit());
                                    calculateTasksForEveryDay3(occurence,repeatEveryNumber,task,groupId);
                                }
                                break;
                            case "week":
                            case "weeks":
                                boolean [] daysOfWeekSelected = childPersonalizedInstance.getDaysSelection();
                                if(!isAfterBtn  && !isTheDayBtn) {
                                    //TODO INIFINITO
                                }
                                if(isTheDayBtn) {
                                    Calendar startDay = DateUtils.
                                            longToCalendarCompleteDate(Long.parseLong(task.getStart()));
                                    Calendar endDay = DateUtils.stringToCalendarLastDay(endDayTv);
                                    int numberDays = getDifference(startDay,endDay);
                                    int divisor = 7 * repeatEveryNumber;
                                    int numberOfRipetitions = numberDays%divisor != 0 ? numberDays/divisor +1
                                            : numberDays/7;
                                    int [] daysOfWeek = getDaysOfWeekSelected(daysOfWeekSelected);
                                    for(int i = 0; i<daysOfWeek.length;i++)
                                        calculateTasksForWeekDays2(daysOfWeek,i,numberOfRipetitions,repeatEveryNumber,
                                                        task,groupId,endDay);
                                }
                                if(isAfterBtn) {
                                    int numberOfRipetitions = Integer.parseInt(
                                            childPersonalizedInstance.getNumberOccorenceEdit());
                                    int [] daysOfWeek = getDaysOfWeekSelected(daysOfWeekSelected);
                                    for(int i = 0; i<daysOfWeek.length;i++)
                                        calculateTasksForWeekDays3(daysOfWeek,i,numberOfRipetitions,repeatEveryNumber
                                                                    ,task,groupId);
                                }
                                    break;
                            case "month":
                            case "months":
                                int occurenceOfDay = getOccurenceOfDayInMonth(childPersonalizedInstance.getMonthSpinner());
                                Calendar endDay = DateUtils.stringToCalendarLastDay(endDayTv);
                                int totalOccurence = Integer.parseInt(childPersonalizedInstance.getNumberOccorenceEdit());
                                if(!isAfterBtn  && !isTheDayBtn) {
                                    //TODO INIFINITO
                                }
                                if(isTheDayBtn)
                                    if(personalizedInstance.getPeriodSelectedPosition()>0)
                                        calculateTasksForEveryMonth2(task,occurenceOfDay,repeatEveryNumber,groupId,endDay);
                                    else
                                        calculateTasksForEveryMonthForDay2(task,endDay,groupId,repeatEveryNumber);
                                if(isAfterBtn)
                                    if(personalizedInstance.getPeriodSelectedPosition()>0)
                                        calculateTasksForEveryMonth3(task,occurenceOfDay,repeatEveryNumber,totalOccurence,
                                                                    groupId);
                                    else
                                        calculateTasksForEveryMonthForDay3(task,groupId,repeatEveryNumber,occurenceOfDay);
                                break;
                            case "year":
                            case "years":
                                if(!isAfterBtn  && !isTheDayBtn) {
                                    //TODO INIFINITO
                                }
                                if(isTheDayBtn) {

                                }
                                if(isAfterBtn) {

                                }
                                break;
                        }

                }
            }
        }

         Task task = new Task(String.valueOf(mCurrentTaskId),title,email,allDay,String.valueOf(mCurrentTaskId),priority,
                category,"PENDING",start,end,"0.0","0.0",
                description,color);

        mRepository.createTask(task, new TasksDataSource.DBCallback() {
            @Override
            public void success() {
                Log.d(TAG, "success: TASK ADDED!");
                String duration = "Today at "+timeStart+" to "+timeEnd;
                updateCurrentTaskId();

                mView.showCalendarView(task.getTitle(),duration,mCurrentTaskId,Long.parseLong(start),priority);
            }

            @Override
            public void failure() {
                Log.d(TAG, "failure: BOOOOOO");
            }
        });
        Log.d(TAG, "saveNewTask:"+task.toString());
    }

    @Override
    public void onChangeTimePicker(int hour, int minute, String [] checkDate, boolean isStartTime) {
        try {
            Date dateForNewTime = DateUtils.formatDataText.parse(checkDate[0]);
            Date tmpTime = DateUtils.formatTimeText.parse(checkDate[2]);
            Date tmpDate = DateUtils.formatDataText.parse(checkDate[1]);
            Date newTime = newPickersElement(dateForNewTime,new int[]{hour,minute});
            Date dateToCompare = dateToCompare(tmpDate,tmpTime);
            mView.updateTime(DateUtils.formatTimeText.format(newTime),
                    checkOrderDate(isStartTime,newTime,dateToCompare));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createPeriodicTask() {

    }


    @Override
    public void createPeriodicTask(String period, int position) {
        getView().updatePeriod(period,position);
    }

    @Override
    public void openPersonalizedPeriodicTask(EditInstanceState state) {
        mView.showPersonalizedView(state);
    }

    @Override
    public void fetchAllCategories() {
        Log.d(TAG, "fetchAllCategories: ");
        mRepository.loadCategories(new TasksDataSource.LoadCategories() {
            @Override
            public void success(List<String> categories) {
                Log.d(TAG, "success: ");
                for(String s:categories)
                    Log.d(TAG, "success: "+s);
                mView.loadAllCategories(categories);
            }

            @Override
            public void failure(String error) {
                Log.d(TAG, "error: ");
                mView.dataError(error);
            }
        });

    }

    @Override
    public void getSelectedCategory(int position, String category) {
        mView.updateCategory(position,category,LOAD_CATEGORY);
    }

    @Override
    public void addCategory(final String category) {

      mRepository.addCategory(category, new TasksDataSource.LoadCategory() {
          @Override
          public void success(String category) {
              Log.d(TAG, "addCategory: success category:"+category);
              mView.loadCategory(category);
          }

          @Override
          public void failure(String error) {
              Log.d(TAG, "addCategory: failure category:"+category);
              mView.dataError(error);
          }
      });
    }

    @Override
    public void deleteCategory(final int position,final String category) {
        Log.d(TAG, "deleteCategory: ");
        mRepository.deleteCategory(category, new TasksDataSource.LoadCategory() {
            @Override
            public void success(String category) {
                mView.updateCategory(position,category,REMOVE_CATEGORY);
            }

            @Override
            public void failure(String error) {
                mView.dataError(error);
            }
        });
    }

    @Override
    public void createRepeatedPeriodText(int repetition, String periodName, boolean[] daysSelected,
                                         String monthNameSelected, int monthPosSelected, boolean isAfterBtn,
                                         boolean isTheDayBtn, String[] periodType, String generalPeriod, int occurrences,                                         String endDayTv) {
        String periodText = getRepeatedPeriodText(repetition, periodName, daysSelected, monthNameSelected,
                            monthPosSelected, isAfterBtn, isTheDayBtn, periodType, generalPeriod, occurrences, endDayTv);


        Log.d(TAG, "createRepeatedPeriodText: repetition ="+repetition+" periodName ="+periodName+"  daysSelected[]                 ="+daysSelected+ "monthNameSelected ="+monthNameSelected+"  monthPosSelected ="+monthPosSelected+"                        isAfterBtn ="+isAfterBtn+ "isTheDayBtn ="+isTheDayBtn+" periodType[] ="+periodType+" generalPeriod                     ="+generalPeriod+ "occurrences ="+occurrences+" endDayTv ="+endDayTv+"");

        mView.updatePeriod(periodText,0);
    }

    @Override
    public void onChangeDatePicker(int year, int month, int dayOfMonth, String [] checkDate, boolean isStartDate) {
        try {
            Date newDateTime = DateUtils.formatTimeText.parse(checkDate[0]);
            Date tmpDate = DateUtils.formatDataText.parse(checkDate[1]);
            Date tmpTime = DateUtils.formatTimeText.parse(checkDate[2]);
            Date newDate = newPickersElement(newDateTime,new int[]{year,month,dayOfMonth});
            Date dateToCompare = dateToCompare(tmpDate,tmpTime);
            mView.updateDate(DateUtils.formatDate(newDate),
                    checkOrderDate(isStartDate,newDate,dateToCompare));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean checkOrderDate(boolean isStart,Date date1, Date date2){
        return ((isStart && date1.compareTo(date2)>0) || (!isStart && date1.compareTo(date2)<0));
    }

    private Date newPickersElement(Date element,int [] params){
        Calendar c = Calendar.getInstance();
        c.setTime(element);
        //SET DATE
        if(params.length > 2){
            c.set(Calendar.YEAR,params[0]);
            c.set(Calendar.MONTH,params[1]);
            c.set(Calendar.DAY_OF_MONTH,params[2]);
        }
        //SET TIME
        else {
            c.set(Calendar.HOUR_OF_DAY,params[0]);
            c.set(Calendar.MINUTE,params[1]);
        }
        return c.getTime();
    }

    private Date dateToCompare(Date date,Date time){
        Calendar cTime = Calendar.getInstance();
        Calendar cDate = (Calendar) cTime.clone();
        cDate.setTime(date);
        cTime.setTime(time);
        cDate.set(Calendar.HOUR_OF_DAY,cTime.get(Calendar.HOUR_OF_DAY));
        cDate.set(Calendar.MINUTE,cTime.get(Calendar.MINUTE));
        return cDate.getTime();
    }

    private String getRepeatedPeriodText(int repetition, String periodName, boolean [] daysSelected,
                                         String monthNameSelected, int monthPosSelected, boolean isAfterBtn, boolean isTheDayBtn,
                                         String [] periodType, String generalPeriod, int occurrences, String endDayTv){
        String res = generalPeriod;
        if(periodName.equals(periodType[0]) || periodName.equals(periodType[3]))
            res = getSpecificPeriod(repetition,periodName,generalPeriod);
        else if(periodName.equals(periodType[1])){
            res = getSpecificPeriod(repetition,periodName,generalPeriod);
            res += " "+getDaysSelected(daysSelected);
        }
        else if(periodName.equals(periodType[2])){
            res = getSpecificPeriod(repetition, periodName,generalPeriod);
            res += ", "+getMonthSelected(monthNameSelected,monthPosSelected);
        }
        res += getRadioChecked(isAfterBtn,isTheDayBtn,occurrences,endDayTv);
        return res;
    }
    private String getSpecificPeriod(int repetition, String periodName, String generalPeriod){
        String periodText = generalPeriod;
        if(repetition == 1)
            periodText = periodText.substring(0,17)+periodName;
        else
            periodText = periodText.substring(0,17)+repetition+" "+periodName;
        return periodText;
    }

    private String getDaysSelected( boolean [] daysSelected){
        boolean atLeastOne = false;
        String daysSelectedText = "on ";
        for(int i = 0; i<daysSelected.length; i++){
            if(daysSelected[i]) {
                daysSelectedText += atLeastOne ? getNameDaySelected(i)+", ":""+getNameDaySelected(i)+", ";
                atLeastOne = true;
            }
        }
        daysSelectedText = daysSelectedText.substring(0,daysSelectedText.length()-2);
        return daysSelectedText;
    }

    private String getNameDaySelected(int day){
        switch (day){
            case 0:
                return "mon";
            case 1:
                return "tue";
            case 2:
                return "wen";
            case 3:
                return "thu";
            case 4:
                return "fri";
            case 5:
                return "sat";
            case 6:
                return "sun";
            default:
                return "error";
        }
    }

    private String getMonthSelected(String monthDaySelected, int positionSelected){
        return  positionSelected > 0 ? "(every "+monthDaySelected.substring(17)+")":"";
    }

    private String getRadioChecked(boolean isAfterBtn,boolean isTheDayBtn, int occurrences, String endDayTv){
        String res;
        if(isAfterBtn){
            switch (occurrences){
                case 1:
                    res = "; for once";
                    break;
                case 2:
                    res = "; twice";
                    break;
                default:
                    res = "; for "+occurrences+" times";
                    break;
            }
        }
        else if(isTheDayBtn){
            String date = DateUtils.formatRepeatedField(DateUtils.parseLastDay(endDayTv));
            res = "; is until "+date;
        }
        else res = "";
        return res;
    }
    @Override
    public void updateCurrentTaskId(){
        mSizeTableTasks = mRepository.getSizeTableTasks();
        mRepository.getLastTaskId(new TasksDataSource.DBCallbackId() {
            @Override
            public void success(int lastId) {
                mCurrentTaskId = mSizeTableTasks > 0 ? lastId+1 : lastId;
                Log.d(TAG, "checkDBVariables: mSizeTableTasks="+mSizeTableTasks+" lastId = "+lastId+" mCurrentTaskId="+mCurrentTaskId);
                Log.d(TAG, "success: UDATED IN CREATE TASK lastId ="+mRepository.getLastId());
            }
        });

    }


    @Override
    public void getTaskById(int taskId) {
        Log.d(TAG, "getTaskById: ");
        mRepository.getTaskById(taskId, new TasksDataSource.DBCallBackTasks() {
            @Override
            public void success(List<Task> tasks) {
                Task task = tasks.get(0);
                Log.d(TAG, "success: ");
                mView.updateViewTaskData(task.getTitle(),task.getEmail(), task.getPriority(),task.getCategory(),task.getStart(),task.getEnd(),
                        task.getDescription(),task.getColor());
            }
        });
    }


    private void calculateTheDayRepetition(Task task, String endDayTv, int repeatEveryNumber, String periodName,
                                           boolean [] daysOfWeek){

        Calendar startDay = DateUtils.stringToCalendarCompleteDate(task.getStart());
        startDay.set(Calendar.HOUR_OF_DAY, 0);
        startDay.set(Calendar.MINUTE,0);

        Calendar endDay = Calendar.getInstance();
        endDay.setTime(DateUtils.parseLastDay(endDayTv));
        endDay.set(Calendar.HOUR_OF_DAY,0);
        endDay.set(Calendar.MINUTE,0);
        endDay.setTimeInMillis(endDay.getTimeInMillis()+dayDuration);

        Log.d(TAG, "createRepeatedPeriodText: endDay --> " +
                "day = "+endDay.get(Calendar.DAY_OF_MONTH)+
                "month ="+endDay.get(Calendar.MONTH)+
                "year = "+endDay.get(Calendar.YEAR)+
                "hour = "+endDay.get(Calendar.HOUR_OF_DAY)+
                "minutes = "+endDay.get(Calendar.MINUTE));


        Log.d(TAG, "createRepeatedPeriodText: AFTER ADDED ONE DAY endDay --> " +
                "day = "+endDay.get(Calendar.DAY_OF_MONTH)+
                "month ="+endDay.get(Calendar.MONTH)+
                "year = "+endDay.get(Calendar.YEAR)+
                "hour = "+endDay.get(Calendar.HOUR_OF_DAY)+
                "minutes = "+endDay.get(Calendar.MINUTE));


        List<Task> tasks = new ArrayList<>();

        switch (periodName){
            case "day":
            case "days":
                long difference = endDay.getTimeInMillis() - startDay.getTimeInMillis();
                int numberDaysOfPeriod = (int) (difference / dayDuration);
                int daysWithoutEvent = repeatEveryNumber -1;
                tasks.addAll(calculateTasksForEveryDay(numberDaysOfPeriod,repeatEveryNumber,
                        repeatEveryNumber-1,task,startDay.getTimeInMillis()));
                break;
            case "week":
            case "weeks":
                int [] daysSelected = calculateDaySelected(daysOfWeek);
                int occurence = (int) (endDay.getTimeInMillis() - startDay.getTimeInMillis()) + 1;
                for(int i = 0; i<daysSelected.length; i++){
                    int dow = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart())).get(Calendar.DAY_OF_WEEK);
                    tasks.addAll(calculateTasksForWeekDays(daysSelected,i,dow,occurence,startDay.getTimeInMillis(),
                                endDay.getTimeInMillis(),task));
                }
                break;
            case "month":
            case "months":



                break;
            case "year":
            case "years":
                break;

        }




    }

    private void calculateOccurenceRepetition(int occurence, int repeatEveryNumber, Calendar startDateTask, Calendar endDateTask){

        int daysWithoutTask = repeatEveryNumber-1;

        Calendar[]startDateDays = new Calendar[occurence];
        Calendar[]endDateDays = new Calendar[occurence];
        for(int i = 0; i<occurence;i++){
            if(i==0) {
                startDateDays[i] = startDateTask;
                endDateDays[i] = endDateTask;
            }
            else {
                Calendar startNextDay = startDateDays[i-1];
                Calendar endNextDay = endDateDays[i-1];
                startNextDay.setTimeInMillis(startNextDay.getTimeInMillis() + (daysWithoutTask * dayDuration));
                endNextDay.setTimeInMillis(endNextDay.getTimeInMillis() + (daysWithoutTask * dayDuration));
                startDateDays[i] = startNextDay;
                endDateDays[i]= endNextDay;
            }

        }
    }

    private List<Task> calculateTasksForEveryDay(int numberDaysOfPeriod, int repeatEveryNumber,int daysWithoutEvent ,Task task, long startDay ){
        List<Task> tasks = new ArrayList<>();
        if(numberDaysOfPeriod > repeatEveryNumber){
            int occurence = numberDaysOfPeriod % repeatEveryNumber !=0 ? numberDaysOfPeriod/repeatEveryNumber +1 :
                    numberDaysOfPeriod/repeatEveryNumber;


            for(int i =0; i<occurence; i++){

                String startDate = i==0 ? task.getStart() :
                        String.valueOf(Long.parseLong(task.getStart()) + (daysWithoutEvent * dayDuration * i));
                String endDate;

                if(!task.isAllDay())
                    endDate = i==0 ? task.getEnd() :
                            String.valueOf(Long.parseLong(task.getEnd()) + (daysWithoutEvent * dayDuration * i));
                else
                    endDate = i==0 ? String.valueOf(startDay+ dayDuration -1) :
                            String.valueOf( startDay+ dayDuration -1 + (daysWithoutEvent * dayDuration * i));

                Task newTask = new Task(task.getId(),task.getTitle(),task.getEmail(),task.isAllDay(),task.getGroupId(),
                        task.getPriority(),task.getCategory(),task.getStatus(),startDate,endDate,
                        task.getLongitude(),task.getLatitude(),task.getDescription(),task.getColor());

                Log.d(TAG, "createRepeatedPeriodText: TASK START DATE = "+DateUtils.stringToCalendarCompleteDate(newTask.getStart()).getTime());
                Log.d(TAG, "createRepeatedPeriodText: TASK END DATE = "+DateUtils.stringToCalendarCompleteDate(newTask.getEnd()).getTime());
                tasks.add(newTask);
            }
        }
        return tasks;
    }

    private List<Task> calculateTasksForWeekDays(int [] daysOfWeek, int index, int dayOfWeek, int occurence,long startDay,
                                                 long endDay, Task task){

        int gap = daysOfWeek[index]==1 ? daysOfWeek[index]+7 - dayOfWeek : daysOfWeek[index] - dayOfWeek;

        List<Task> tasks = new ArrayList<>();

        for(int i = 0; i < occurence; i++){
            String startDate = i==0 ?  String.valueOf(Long.parseLong(task.getStart()) + (gap * dayDuration)) :
                    String.valueOf(Long.parseLong(task.getStart()) + gap*dayDuration + weekDuration*i);
            String endDate;

            if(!task.isAllDay())
                endDate = i==0 ? String.valueOf(Long.parseLong(task.getEnd()) + (gap * dayDuration)) :
                    String.valueOf(Long.parseLong(task.getEnd()) + gap*dayDuration + weekDuration*i);
            else
                endDate = i==0 ? String.valueOf(startDay + dayDuration -1) :
                        String.valueOf(startDay + dayDuration -1 + gap*dayDuration + weekDuration*i);

            if(Long.parseLong(startDate) <= endDay && Long.parseLong(startDate)>= Long.parseLong(task.getStart())){

                Task newTask = new Task(task.getId(),task.getTitle(),task.getEmail(),task.isAllDay(),task.getGroupId(),
                        task.getPriority(),task.getCategory(),task.getStatus(),startDate,endDate,
                        task.getLongitude(),task.getLatitude(),task.getDescription(),task.getColor());

                Log.d(TAG, "calculateTheDayRepetition: task ="+task.toString());
                Log.d(TAG, "calculateTasksForWeekDays: endDay = "+DateUtils.longToStringCompleteInformationDate(endDay));

                tasks.add(newTask);
            }
        }

        return tasks;
    }

    private int [] calculateDaySelected(boolean [] daysOfWeek){
        int [] daysSelected = new int[daysOfWeek.length];
        Arrays.fill(daysSelected,0);

        int j = 0;
        for(int i = 0; i < daysOfWeek.length; i++){
            if(daysOfWeek[i]){
                if(i <= 5)
                    daysSelected[j]=i+2;
                else
                    daysSelected[j]=1;
                j++;
            }
        }
        int [] result = new int[j];
        for(int i = 0; i < result.length; i++){
            result[i]=daysSelected[i];
            Log.d(TAG, "calculateDaySelected: result["+i+"]="+result[i]);
        }
        return result;
    }

    private List<Task>  calculateTasksForEveryDay2(Task task,Calendar lastDay,int repeatEveryNumber, String groupId){
        List<Task> tasks = new ArrayList<>();

        Calendar start;
        Calendar end;
        if(!task.isAllDay()) {
            start = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            end = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getEnd()));
        }
        else{
            start = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            start.set(Calendar.HOUR_OF_DAY,0);
            start.set(Calendar.MINUTE,0);
            end = copyCalendar(start);
            start.set(Calendar.HOUR_OF_DAY,23);
            start.set(Calendar.MINUTE,59);
        }

        int rangeDays = getDifference(start,lastDay);
        int numberRepetition = rangeDays % repeatEveryNumber != 0 ? rangeDays/repeatEveryNumber + 1:
                rangeDays/repeatEveryNumber;

        Calendar currentStart = copyCalendar(start);
        Calendar currentEnd = copyCalendar(end);

        int currentDay = currentStart.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentStart.get(Calendar.MONTH);
        int currentYear = currentStart.get(Calendar.YEAR);
        int currentLastDayOfMonth = getLastDayOfMonth(currentMonth,currentYear);

        for(int i = 0; i<numberRepetition;i++){
            if(i > 0){
                if(currentDay + repeatEveryNumber > currentLastDayOfMonth){
                    currentYear += currentMonth+1 > Calendar.DECEMBER ? 1 : 0;
                    currentMonth = currentMonth+1 > Calendar.DECEMBER ? Calendar.JANUARY : currentMonth+1;
                    currentDay = (currentDay+repeatEveryNumber) - currentLastDayOfMonth;
                    currentLastDayOfMonth = getLastDayOfMonth(currentMonth,currentYear);
                }
                else
                    currentDay += repeatEveryNumber;
                modifyCalendar(currentStart,currentDay,currentMonth,currentYear,-1,-1);
                modifyCalendar(currentEnd,currentDay,currentMonth,currentYear,-1,-1);
            }
            Task task1 = createNewTask(task,groupId,currentStart.getTimeInMillis(),currentEnd.getTimeInMillis());
            tasks.add(task1);
        }
        return tasks;
    }

    private List<Task> calculateTasksForWeekDays2(int [] daysOfWeek, int index, int numberOfRipetitions,int repeatEveryNumber, Task task,
                                                 String groupId,Calendar endDay){
        List<Task> tasks = new ArrayList<>();

        Calendar currentFirst = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
        currentFirst.set(Calendar.DAY_OF_MONTH,1);
        Calendar currentStart;
        Calendar currentEnd;

        if(!task.isAllDay()){
            currentStart = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            currentEnd = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getEnd()));
        }
        else {
            currentStart = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            currentEnd = copyCalendar(currentStart);
            currentStart.set(Calendar.HOUR_OF_DAY,0);
            currentStart.set(Calendar.MINUTE,0);
            currentStart.set(Calendar.HOUR_OF_DAY,23);
            currentStart.set(Calendar.MINUTE,59);
        }


        int currentFirstDay = currentFirst.get(Calendar.DAY_OF_MONTH);
        int currentFirstMonth = currentFirst.get(Calendar.MONTH);
        int currentFirstYear = currentFirst.get(Calendar.YEAR);
        int currentFirstLastDayMonth = getLastDayOfMonth(currentFirstMonth,currentFirstYear);
        int currentDay;
        int dowFirst = currentFirst.get(Calendar.DAY_OF_WEEK);
        int dow = daysOfWeek[index];
        int gap = dow - dowFirst;
        long taskStartTime = Long.parseLong(task.getStart());
        long endDayTime = endDay.getTimeInMillis();
        for(int i = 0; i<numberOfRipetitions; i++){

            if(currentFirst.getTimeInMillis() < taskStartTime && gap<0){
                i--;
                if(currentFirstDay + 7 > currentFirstLastDayMonth){
                    currentFirstYear += currentFirstMonth+1 > Calendar.DECEMBER ? 1 : 0;
                    currentFirstMonth = currentFirstMonth+1 > Calendar.DECEMBER ? Calendar.JANUARY : currentFirstMonth+1;
                    currentFirstDay = 1;
                    currentFirstLastDayMonth = getLastDayOfMonth(currentFirstMonth,currentFirstYear);
                }
                else
                    currentFirstDay += 7;
            }
            else {



                //modify currentFirstDay
                if(currentFirstDay + 7*repeatEveryNumber > currentFirstLastDayMonth){
                    currentFirstYear += currentFirstMonth+1 > Calendar.DECEMBER ? 1 : 0;
                    currentFirstMonth = currentFirstMonth+1 > Calendar.DECEMBER ? Calendar.JANUARY : currentFirstMonth+1;
                    currentFirstDay = 1;
                    currentFirstLastDayMonth = getLastDayOfMonth(currentFirstMonth,currentFirstYear);
                }
                else
                    currentFirstDay += 7*repeatEveryNumber;

                modifyCalendar(currentFirst,currentFirstDay,currentFirstMonth,currentFirstYear,-1,-1);
                if(currentFirstDay==1){
                    dowFirst = currentFirst.get(Calendar.DAY_OF_WEEK);
                    gap = dow - dowFirst;
                }
            }
            modifyCalendar(currentFirst,currentFirstDay,currentFirstMonth,currentFirstYear,-1,-1);
            currentDay = currentFirstDay + gap;
            modifyCalendar(currentStart,currentDay,currentFirstMonth,currentFirstYear,-1,-1);
            modifyCalendar(currentEnd,currentDay,currentFirstMonth,currentFirstYear,-1,-1);
            //check currentDay
            if(currentStart.getTimeInMillis()>= taskStartTime
                    && currentStart.getTimeInMillis()<endDayTime
                    && currentStart.get(Calendar.DAY_OF_WEEK)==dow
                    && System.currentTimeMillis() < currentStart.getTimeInMillis()) {
                Log.d(TAG, "calculateTasksForWeekDays: ADDED!! ="+currentStart.getTime());
                Task newTask = createNewTask(task, groupId, currentStart.getTimeInMillis(), currentEnd.getTimeInMillis());
                tasks.add(newTask);
            }
        }
        return tasks;
    }
    private List<Task> calculateTasksForEveryMonth2(Task task,int numberWeek, int everyN, String groupId ,Calendar endDay){
        List<Task> tasks = new ArrayList<>();
        Calendar cStart;
        Calendar cEnd;

        if(!task.isAllDay()){
            cStart = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            cEnd = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getEnd()));
        }
        else {
            cStart = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            cEnd = copyCalendar(cStart);
            cStart.set(Calendar.HOUR_OF_DAY,0);
            cStart.set(Calendar.MINUTE,0);
            cStart.set(Calendar.HOUR_OF_DAY,23);
            cStart.set(Calendar.MINUTE,59);
        }
        Calendar firstDayOfMonth = copyCalendar(cStart);
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH,1);
        firstDayOfMonth.set(Calendar.HOUR_OF_DAY,0);
        firstDayOfMonth.set(Calendar.MINUTE,0);




        int fDay = firstDayOfMonth.get(Calendar.DAY_OF_MONTH);
        int fMonth = firstDayOfMonth.get(Calendar.MONTH);
        int fYear = firstDayOfMonth.get(Calendar.YEAR);
        int fLastDay = getLastDayOfMonth(fMonth,fYear);

        Calendar lastDayOfMonth = copyCalendar(cStart);
        lastDayOfMonth.set(Calendar.DAY_OF_MONTH,getLastDayOfMonth(fMonth,fYear));
        lastDayOfMonth.set(Calendar.HOUR_OF_DAY,0);
        lastDayOfMonth.set(Calendar.MINUTE,0);

        int lDay = lastDayOfMonth.get(Calendar.DAY_OF_MONTH);
        int lMonth = lastDayOfMonth.get(Calendar.MONTH);
        int lYear = lastDayOfMonth.get(Calendar.YEAR);

        int cDay;

        int dowTask = cStart.get(Calendar.DAY_OF_WEEK);
        int dowFirst = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);
        int dowLast = lastDayOfMonth.get(Calendar.DAY_OF_WEEK);


        int fGap = dowTask - dowFirst;
        int lGap = dowTask - dowLast;

        int endMonth = endDay.get(Calendar.MONTH);
        int endYear = endDay.get(Calendar.YEAR);
        long taskStartTime = Long.parseLong(task.getStart());

        int countOccurence = 0;
        cStart.set(Calendar.DAY_OF_MONTH,1);


        Log.d(TAG, "calculateTasksForEveryMonth: first="+firstDayOfMonth.getTime());
        for(int i = 0; i< 5; i++){
            //last
            if((endMonth<fMonth && endYear==fYear)|| (endMonth<lMonth && endYear==lYear)){
                Log.d(TAG, numberWeek==-1 ? "calculateTasksForEveryMonth: BREAK endMonth="+endMonth+"  lastMonth="+lMonth:
                        "calculateTasksForEveryMonth: BREAK endMonth="+endMonth+" firstMonth="+fMonth);
                break;
            }
            Log.d(TAG, "calculateTasksForEveryMonth: CICLO ="+i);
            if(numberWeek == -1){
                cDay = lDay + lGap;
                modifyCalendar(cStart,cDay,lMonth,lYear,-1,-1);
                modifyCalendar(cEnd,cDay,lMonth,lYear,-1,-1);
                Log.d(TAG, "calculateTasksForEveryMonth: cStart="+cStart.getTime());
                if(cStart.get(Calendar.DAY_OF_WEEK) == dowTask
                        && cStart.get(Calendar.MONTH ) == lMonth
                        && cStart.get(Calendar.YEAR) == lYear
                        && taskStartTime <= cStart.getTimeInMillis()){
                    tasks.add(createNewTask(task,groupId,cStart.getTimeInMillis(),cEnd.getTimeInMillis()));
                    Log.d(TAG, "calculateTasksForEveryMonth: TASK ADDED!!!");
                    lYear += lMonth+everyN > Calendar.DECEMBER ? 1:0;
                    lMonth = lMonth+everyN > Calendar.DECEMBER  ? (lMonth+everyN) -Calendar.UNDECIMBER : lMonth+everyN;
                    lDay = getLastDayOfMonth(lMonth,lYear);
                    i=-1;
                    modifyCalendar(lastDayOfMonth,lDay,lMonth,lYear,-1,-1);
                    dowLast = lastDayOfMonth.get(Calendar.DAY_OF_WEEK);
                    lGap  = dowTask - dowLast;
                    Log.d(TAG, "calculateTasksForEveryMonth: CHANGE MONTH ="+lastDayOfMonth.getTime());
                    continue;
                }
                lDay += -7;
                modifyCalendar(lastDayOfMonth,lDay,lMonth,lYear,-1,-1);
                Log.d(TAG, "calculateTasksForEveryMonth: last="+lastDayOfMonth.getTime());
            }
            //dayOccurenceInTheMonth
            else {


                cDay = fDay + fGap;
                modifyCalendar(cStart, cDay, fMonth, fYear, -1, -1);
                modifyCalendar(cEnd, cDay, fMonth, fYear, -1, -1);
                if(cStart.get(Calendar.DAY_OF_WEEK) == dowTask
                        && cStart.get(Calendar.MONTH ) == fMonth
                        && cStart.get(Calendar.YEAR) == fYear)
                    countOccurence++;

                if(cStart.get(Calendar.DAY_OF_WEEK) == dowTask
                        && cStart.get(Calendar.MONTH ) == fMonth
                        && cStart.get(Calendar.YEAR) == fYear
                        && countOccurence == numberWeek
                        && taskStartTime <= cStart.getTimeInMillis()) {
                    tasks.add(createNewTask(task, groupId, cStart.getTimeInMillis(), cEnd.getTimeInMillis()));
                    Log.d(TAG, "calculateTasksForEveryMonth: TASK ADDED!!!");

                    fYear += fMonth + everyN > Calendar.DECEMBER ? 1 : 0;
                    fMonth = fMonth + everyN > Calendar.DECEMBER ? (fMonth+ everyN) -Calendar.UNDECIMBER : fMonth + everyN;
                    fDay = 1;
                    fLastDay = getLastDayOfMonth(fMonth, fYear);
                    i=-1;
                    modifyCalendar(firstDayOfMonth,fDay,fMonth,fYear,-1,-1);
                    dowFirst = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);
                    fGap = dowTask - dowFirst;
                    countOccurence=0;
                    Log.d(TAG, "calculateTasksForEveryMonth: CHANGE MONTH ="+firstDayOfMonth.getTime());
                    continue;
                }

                fDay += 7;
                modifyCalendar(firstDayOfMonth,fDay,fMonth,fYear,-1,-1);
                Log.d(TAG, "calculateTasksForEveryMonth: first="+firstDayOfMonth.getTime());
            }
        }
        for(Task t: tasks)
            Log.d(TAG, "calculateTasksForEveryMonth: t ="+DateUtils.longToCalendarCompleteDate(Long.parseLong(t.getStart())).getTime());
        return tasks;
    }

    private List<Task> calculateTasksForEveryMonthForDay2(Task task,Calendar endDay,String groupId,int everyN){
        List<Task> tasks =new ArrayList<>();

        Calendar start;
        Calendar end;

        if(!task.isAllDay()){
            start = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            end = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getEnd()));
        }
        else {
            start = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            end = copyCalendar(start);
            start.set(Calendar.HOUR_OF_DAY,0);
            start.set(Calendar.MINUTE,0);
            end.set(Calendar.HOUR_OF_DAY,23);
            end.set(Calendar.MINUTE,59);
        }
        int sDay = start.get(Calendar.DAY_OF_MONTH);
        int sMonth = start.get(Calendar.MONTH);
        int sYear = start.get(Calendar.YEAR);

        int eMonth = endDay.get(Calendar.MONTH);
        int eYear = endDay.get(Calendar.YEAR);
        int numberOfRipetitions = eYear == sYear ? eMonth - sMonth : eMonth+Calendar.UNDECIMBER - sMonth;
        Log.d(TAG, "calculateTasksForEveryMonthForDay: endMonth="+eMonth+" sMonth"+sMonth);
        for(int i = 0; i < numberOfRipetitions+1;i++){
            Log.d(TAG, "calculateTasksForEveryMonthForDay: START="+start.getTime()+" i="+i);
            if(start.getTimeInMillis()<endDay.getTimeInMillis()){
                tasks.add(createNewTask(task,groupId,start.getTimeInMillis(),end.getTimeInMillis()));
                sYear+= sMonth+everyN > Calendar.DECEMBER ? 1:0;
                sMonth = sMonth+everyN > Calendar.DECEMBER ? (sMonth+everyN)-Calendar.UNDECIMBER: sMonth+everyN;
                modifyCalendar(start,sDay,sMonth,sYear,-1,-1);
                modifyCalendar(end,sDay,sMonth,sYear,-1,-1);
            }
        }
        for(Task t: tasks)
            Log.d(TAG, "calculateTasksForEveryMonthForDay: t="+DateUtils.longToCalendarCompleteDate(Long.parseLong(t.getStart())).getTime());
        return tasks;
    }


    private List<Task> calculateTasksForEveryDay3(int numberOfRipetitions, int repeatEveryNumber,Task task,String groupId){
        List<Task> result = new ArrayList<>();
        Calendar start;
        Calendar end;

        if(!task.isAllDay()){
            start = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            end = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getEnd()));
        }
        else {
            start = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            end = copyCalendar(start);
            start.set(Calendar.HOUR_OF_DAY,0);
            start.set(Calendar.MINUTE,0);
            end.set(Calendar.HOUR_OF_DAY,23);
            end.set(Calendar.MINUTE,59);
        }
        Calendar currentStart = copyCalendar(start);
        Calendar currentEnd = copyCalendar(end);
        int currentDay = currentStart.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentStart.get(Calendar.MONTH);
        int currentYear = currentStart.get(Calendar.YEAR);
        int currentLastDayOfMonth = getLastDayOfMonth(currentMonth,currentYear);
        for(int i=0; i<numberOfRipetitions; i++){
            if(i>0){
                if(currentDay + repeatEveryNumber > currentLastDayOfMonth ){
                    currentYear += currentMonth+1 > Calendar.DECEMBER ? 1 : 0;
                    currentMonth = currentMonth+1 > Calendar.DECEMBER ? Calendar.JANUARY : currentMonth+1;
                    currentDay = (currentDay+repeatEveryNumber) - currentLastDayOfMonth;
                    currentLastDayOfMonth = getLastDayOfMonth(currentMonth,currentYear);
                }
                else
                    currentDay += repeatEveryNumber;
                modifyCalendar(currentStart,currentDay,currentMonth,currentYear,-1,-1);
                modifyCalendar(currentEnd,currentDay,currentMonth,currentYear,-1,-1);
            }
            Task newTask = createNewTask(task,groupId,currentStart.getTimeInMillis(),currentEnd.getTimeInMillis());
            result.add(newTask);
        }
        for(Task t:result)
            Log.d(TAG, "calculateOccurenceRepetition: t ="+DateUtils.longToCalendarCompleteDate(Long.parseLong(t.getStart())).getTime());
        return result;
    }

    private List<Task> calculateTasksForWeekDays3(int [] daysOfWeek, int index, int numberOfRipetitions,int repeatEveryNumber, Task task,
                                                  String groupId){
        List<Task> tasks = new ArrayList<>();

        Calendar currentFirst = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
        currentFirst.set(Calendar.DAY_OF_MONTH,1);
        Calendar currentStart;
        Calendar currentEnd;

        if(!task.isAllDay()){
            currentStart = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            currentEnd = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getEnd()));
        }
        else {
            currentStart = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            currentEnd = copyCalendar(currentStart);
            currentStart.set(Calendar.HOUR_OF_DAY,0);
            currentStart.set(Calendar.MINUTE,0);
            currentStart.set(Calendar.HOUR_OF_DAY,23);
            currentStart.set(Calendar.MINUTE,59);
        }


        int currentFirstDay = currentFirst.get(Calendar.DAY_OF_MONTH);
        int currentFirstMonth = currentFirst.get(Calendar.MONTH);
        int currentFirstYear = currentFirst.get(Calendar.YEAR);
        int currentFirstLastDayMonth = getLastDayOfMonth(currentFirstMonth,currentFirstYear);
        int currentDay;
        int dowFirst = currentFirst.get(Calendar.DAY_OF_WEEK);
        int dow = daysOfWeek[index];
        int gap = dow - dowFirst;
        long taskStartTime = Long.parseLong(task.getStart());
        for(int i = 0; i<numberOfRipetitions+1; i++){

            if(currentFirst.getTimeInMillis() < taskStartTime && gap<0){
                i--;
                if(currentFirstDay + 7 > currentFirstLastDayMonth){
                    currentFirstYear += currentFirstMonth+1 > Calendar.DECEMBER ? 1 : 0;
                    currentFirstMonth = currentFirstMonth+1 > Calendar.DECEMBER ? Calendar.JANUARY : currentFirstMonth+1;
                    currentFirstDay = 1;
                    currentFirstLastDayMonth = getLastDayOfMonth(currentFirstMonth,currentFirstYear);
                }
                else
                    currentFirstDay += 7;
            }
            else {



                //modify currentFirstDay
                if(currentFirstDay + 7*repeatEveryNumber > currentFirstLastDayMonth){
                    currentFirstYear += currentFirstMonth+1 > Calendar.DECEMBER ? 1 : 0;
                    currentFirstMonth = currentFirstMonth+1 > Calendar.DECEMBER ? Calendar.JANUARY : currentFirstMonth+1;
                    currentFirstDay = 1;
                    currentFirstLastDayMonth = getLastDayOfMonth(currentFirstMonth,currentFirstYear);
                }
                else
                    currentFirstDay += 7*repeatEveryNumber;

                modifyCalendar(currentFirst,currentFirstDay,currentFirstMonth,currentFirstYear,-1,-1);
                if(currentFirstDay==1){
                    dowFirst = currentFirst.get(Calendar.DAY_OF_WEEK);
                    gap = dow - dowFirst;
                }
            }
            modifyCalendar(currentFirst,currentFirstDay,currentFirstMonth,currentFirstYear,-1,-1);
            currentDay = currentFirstDay + gap;
            modifyCalendar(currentStart,currentDay,currentFirstMonth,currentFirstYear,-1,-1);
            modifyCalendar(currentEnd,currentDay,currentFirstMonth,currentFirstYear,-1,-1);
            //check currentDay
            if(currentStart.getTimeInMillis()>= taskStartTime
                    && currentStart.get(Calendar.DAY_OF_WEEK)==dow
                    && System.currentTimeMillis() < currentStart.getTimeInMillis()
                    && tasks.size()<numberOfRipetitions) {
                Log.d(TAG, "calculateTasksForWeekDays: ADDED!! ="+currentStart.getTime());
                Task newTask = createNewTask(task, groupId, currentStart.getTimeInMillis(), currentEnd.getTimeInMillis());
                tasks.add(newTask);
            }
        }
        return tasks;
    }
    private List<Task> calculateTasksForEveryMonth3(Task task, int numberWeek,int everyN, int totalOccurence, String groupId){
        List<Task> tasks = new ArrayList<>();
        Calendar cStart;
        Calendar cEnd;

        if(!task.isAllDay()){
            cStart = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            cEnd = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getEnd()));
        }
        else {
            cStart = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            cEnd = copyCalendar(cStart);
            cStart.set(Calendar.HOUR_OF_DAY,0);
            cStart.set(Calendar.MINUTE,0);
            cStart.set(Calendar.HOUR_OF_DAY,23);
            cStart.set(Calendar.MINUTE,59);
        }
        Calendar firstDayOfMonth = copyCalendar(cStart);
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH,1);
        firstDayOfMonth.set(Calendar.HOUR_OF_DAY,0);
        firstDayOfMonth.set(Calendar.MINUTE,0);

        int fDay = firstDayOfMonth.get(Calendar.DAY_OF_MONTH);
        int fMonth = firstDayOfMonth.get(Calendar.MONTH);
        int fYear = firstDayOfMonth.get(Calendar.YEAR);
        int fLastDay = getLastDayOfMonth(fMonth,fYear);

        Calendar lastDayOfMonth = copyCalendar(cStart);
        lastDayOfMonth.set(Calendar.DAY_OF_MONTH,getLastDayOfMonth(fMonth,fYear));
        lastDayOfMonth.set(Calendar.HOUR_OF_DAY,0);
        lastDayOfMonth.set(Calendar.MINUTE,0);

        int lDay = lastDayOfMonth.get(Calendar.DAY_OF_MONTH);
        int lMonth = lastDayOfMonth.get(Calendar.MONTH);
        int lYear = lastDayOfMonth.get(Calendar.YEAR);

        int cDay;

        int dowTask = cStart.get(Calendar.DAY_OF_WEEK);
        int dowFirst = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);
        int dowLast = lastDayOfMonth.get(Calendar.DAY_OF_WEEK);


        int fGap = dowTask - dowFirst;
        int lGap = dowTask - dowLast;


        long taskStartTime = Long.parseLong(task.getStart());

        int countOccurence = 0;
        cStart.set(Calendar.DAY_OF_MONTH,1);

        Log.d(TAG, "calculateTasksForEveryMonth: first="+firstDayOfMonth.getTime());
        for(int i = 0; i< 5; i++){
            //last
            if( totalOccurence==0)
                break;

            Log.d(TAG, "calculateTasksForEveryMonth: CICLO ="+i);
            if(numberWeek == -1){
                cDay = lDay + lGap;
                modifyCalendar(cStart,cDay,lMonth,lYear,-1,-1);
                modifyCalendar(cEnd,cDay,lMonth,lYear,-1,-1);
                Log.d(TAG, "calculateTasksForEveryMonth: cStart="+cStart.getTime());
                if(cStart.get(Calendar.DAY_OF_WEEK) == dowTask
                        && cStart.get(Calendar.MONTH ) == lMonth
                        && cStart.get(Calendar.YEAR) == lYear
                        && taskStartTime <= cStart.getTimeInMillis()){
                    tasks.add(createNewTask(task,groupId,cStart.getTimeInMillis(),cEnd.getTimeInMillis()));
                    Log.d(TAG, "calculateTasksForEveryMonth: TASK ADDED!!!");
                    lYear += lMonth+everyN > Calendar.DECEMBER ? 1:0;
                    lMonth = lMonth+everyN > Calendar.DECEMBER  ? (lMonth+everyN) -Calendar.UNDECIMBER : lMonth+everyN;
                    lDay = getLastDayOfMonth(lMonth,lYear);
                    i=-1;
                    modifyCalendar(lastDayOfMonth,lDay,lMonth,lYear,-1,-1);
                    dowLast = lastDayOfMonth.get(Calendar.DAY_OF_WEEK);
                    lGap  = dowTask - dowLast;
                    Log.d(TAG, "calculateTasksForEveryMonth: CHANGE MONTH ="+lastDayOfMonth.getTime());
                    totalOccurence--;
                    continue;
                }
                lDay += -7;
                modifyCalendar(lastDayOfMonth,lDay,lMonth,lYear,-1,-1);
                Log.d(TAG, "calculateTasksForEveryMonth: last="+lastDayOfMonth.getTime());
            }
            //dayOccurenceInTheMonth
            else {


                cDay = fDay + fGap;
                modifyCalendar(cStart, cDay, fMonth, fYear, -1, -1);
                modifyCalendar(cEnd, cDay, fMonth, fYear, -1, -1);
                if(cStart.get(Calendar.DAY_OF_WEEK) == dowTask
                        && cStart.get(Calendar.MONTH ) == fMonth
                        && cStart.get(Calendar.YEAR) == fYear){
                    countOccurence++;
                    Log.d(TAG, "calculateTasksForEveryMonth3: START="+cStart.getTime()+" COUNT="+countOccurence);
                }

                if(cStart.get(Calendar.DAY_OF_WEEK) == dowTask
                        && cStart.get(Calendar.MONTH ) == fMonth
                        && cStart.get(Calendar.YEAR) == fYear
                        && countOccurence == numberWeek
                        && taskStartTime <= cStart.getTimeInMillis()) {
                    tasks.add(createNewTask(task, groupId, cStart.getTimeInMillis(), cEnd.getTimeInMillis()));
                    Log.d(TAG, "calculateTasksForEveryMonth: TASK ADDED!!!");

                    fYear += fMonth + everyN > Calendar.DECEMBER ? 1 : 0;
                    fMonth = fMonth + everyN > Calendar.DECEMBER ? (fMonth+ everyN) -Calendar.UNDECIMBER : fMonth + everyN;
                    fDay = 1;
                    fLastDay = getLastDayOfMonth(fMonth, fYear);
                    i=-1;
                    modifyCalendar(firstDayOfMonth,fDay,fMonth,fYear,-1,-1);
                    dowFirst = firstDayOfMonth.get(Calendar.DAY_OF_WEEK);
                    fGap = dowTask - dowFirst;
                    countOccurence=0;
                    Log.d(TAG, "calculateTasksForEveryMonth: CHANGE MONTH ="+firstDayOfMonth.getTime());
                    totalOccurence--;
                    continue;
                }

                fDay += 7;
                modifyCalendar(firstDayOfMonth,fDay,fMonth,fYear,-1,-1);
                Log.d(TAG, "calculateTasksForEveryMonth: first="+firstDayOfMonth.getTime());
            }
        }
        for(Task t: tasks)
            Log.d(TAG, "calculateTasksForEveryMonth: t ="+DateUtils.longToCalendarCompleteDate(Long.parseLong(t.getStart())).getTime());
        return tasks;
    }

    private List<Task> calculateTasksForEveryMonthForDay3(Task task,String groupId,int everyN,int occurenceOfMonth){
        List<Task> tasks =new ArrayList<>();

        Calendar start;
        Calendar end;

        if(!task.isAllDay()){
            start = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            end = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getEnd()));
        }
        else {
            start = DateUtils.longToCalendarCompleteDate(Long.parseLong(task.getStart()));
            end = copyCalendar(start);
            start.set(Calendar.HOUR_OF_DAY,0);
            start.set(Calendar.MINUTE,0);
            end.set(Calendar.HOUR_OF_DAY,23);
            end.set(Calendar.MINUTE,59);
        }
        int sDay = start.get(Calendar.DAY_OF_MONTH);
        int sMonth = start.get(Calendar.MONTH);
        int sYear = start.get(Calendar.YEAR);

        for(int i = 0; i < occurenceOfMonth;i++){
            tasks.add(createNewTask(task,groupId,start.getTimeInMillis(),end.getTimeInMillis()));
            sYear+= sMonth+everyN > Calendar.DECEMBER ? 1:0;
            sMonth = sMonth+everyN > Calendar.DECEMBER ? (sMonth+everyN)-Calendar.UNDECIMBER: sMonth+everyN;
            modifyCalendar(start,sDay,sMonth,sYear,-1,-1);
            modifyCalendar(end,sDay,sMonth,sYear,-1,-1);
        }
        for(Task t: tasks)
            Log.d(TAG, "calculateTasksForEveryMonthForDay: t="+DateUtils.longToCalendarCompleteDate(Long.parseLong(t.getStart())).getTime());
        return tasks;
    }



    private Task createNewTask(Task task,String groupId,long startTime,long endTime){
        String startDate = String.valueOf(startTime);
        String endDate = String.valueOf(endTime);

        return new Task(task.getId(),task.getTitle(),task.getEmail(),task.isAllDay(),groupId,task.getPriority(),
                task.getCategory(),task.getStatus(),startDate,endDate,task.getLongitude(),task.getLatitude(),
                task.getDescription(),task.getColor());
    }

    private void modifyCalendar(Calendar c,int day, int month, int year,int hour, int minute){
        if(day!=-1)
            c.set(Calendar.DAY_OF_MONTH,day);
        if(month!=-1)
            c.set(Calendar.MONTH,month);
        if(year!=-1)
            c.set(Calendar.YEAR,year);
        if(hour!=-1)
            c.set(Calendar.HOUR_OF_DAY,hour);
        if(minute!=-1)
            c.set(Calendar.MINUTE,minute);
    }

    private int getDifference(Calendar smaller, Calendar bigger){
        int smallerDom = smaller.get(Calendar.DAY_OF_MONTH);
        int biggerDom = bigger.get(Calendar.DAY_OF_MONTH);

        int smallerMonth = smaller.get(Calendar.MONTH);
        int biggerMonth = bigger.get(Calendar.MONTH);

        int smallerYear = smaller.get(Calendar.YEAR);
        int biggerYear = bigger.get(Calendar.YEAR);

        if(smallerMonth == biggerMonth)
            return biggerDom - smallerDom;

        else{

            int numberMonths = biggerYear>smallerYear ? 12 + biggerMonth - smallerMonth:
                    biggerMonth - smallerMonth;
            int numberOfDays = 0;

            for(int i = 0 ; i < numberMonths; i++){
                if(i==0)
                    numberOfDays = getLastDayOfMonth(smallerMonth, smaller.get(Calendar.YEAR)) - smallerDom;
                else
                    numberOfDays += smallerMonth+i == Calendar.JANUARY ?
                            getLastDayOfMonth(smallerMonth + i, biggerYear):
                            getLastDayOfMonth(smallerMonth + i, smallerYear);
                if(i+1 == numberMonths)
                    numberOfDays += biggerDom;
            }
            return numberOfDays;
        }
    }

    private int getLastDayOfMonth(int month, int year){
        int result = -1;
        switch (month){
            case Calendar.NOVEMBER:
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
                result = 30;
                break;
            case Calendar.FEBRUARY:
                if((year%4 == 0 && year%100 == 0 && year%400 == 0) || (year%4==0 && year%100!=0))
                    result = 29;
                else
                    result = 28;
                break;
            default:
                result = 31;
        }
        return result;
    }

    private Calendar copyCalendar(Calendar calendar){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
        c.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY));
        c.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE));
        return c;
    }

    private int [] getDaysOfWeekSelected(boolean [] daysOfWeekSelected){
        int [] tmp = new int[daysOfWeekSelected.length];
        int j= 0;
        for(int i = 0; i<daysOfWeekSelected.length;i++)
            if(daysOfWeekSelected[i]){
                if(i+1 == daysOfWeekSelected.length){
                    tmp[j]=1;
                }
                else
                    tmp[j]=i+2;
                j++;
            }
        int[]result = new int[j];
        for(int ii =0; ii<j; ii++)
            result[ii]=tmp[ii];
        return result;
    }

    private int getOccurenceOfDayInMonth(String data){
        if(data.contains("first"))
            return 1;
        else if(data.contains("second"))
            return 2;
        else if(data.contains("third"))
            return 3;
        else if(data.contains("fourth"))
            return 4;
        else
            return -1;
    }
}
