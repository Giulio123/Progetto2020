package it.guaraldi.to_dotaskmanager.ui.graphic;

import java.util.List;

import it.guaraldi.to_dotaskmanager.data.local.entities.Task;
import it.guaraldi.to_dotaskmanager.ui.base.IBaseView;

public interface GraphicContract {

    interface View extends IBaseView{
        void getTasksForGraphic(String category, int []totalTask, int []taskPending , int [] taskComplete);
    }

    interface Presenter{
        void getAllTasksByCategory(String category);
    }
}
