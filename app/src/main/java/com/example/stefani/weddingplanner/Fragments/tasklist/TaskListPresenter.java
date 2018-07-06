package com.example.stefani.weddingplanner.Fragments.tasklist;

import com.example.stefani.weddingplanner.Net.ConnectionToFirebase;
import com.example.stefani.weddingplanner.BasicClasses.Constants;
import com.example.stefani.weddingplanner.BasicClasses.TaskClass;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskListPresenter implements ITaskList.Presenter {

    private ITaskList.View mView;
    public static List<TaskClass> mTaskClassList;

    public TaskListPresenter(ITaskList.View guestDetailsFragmentView) {
        mView = guestDetailsFragmentView;
        getAllDetailsFromDB();
        checkFirstTime();
    }

    private void checkFirstTime() {
        // if this is first installation of the app, no data is in db.
        if (mTaskClassList == null || mTaskClassList.isEmpty()) { // task list is empty at first
            toDoList();
        }
    }

    private void toDoList() {
        // insert basic task into db
        String taskName[] = {"Make a list of guests", "Choose a wedding hall", "Choose a makeup artist and hair stylist", "Choose a photographer",
                "Choose a DJ", "Choose a Rabbi", "Open a file in the rabbinate", "Buy / rent a wedding dress", "Buy a groom's suit and shoes",
                "Pass bridal guidance", "Buy rings", "Rent a car and select driver", "Design and print wedding invitations", "Buy bridal shoes", "Buy jewelry",
                "Choose a place to get ready on the wedding day", "Give out invitations", "Choose a bridal bouquet", "Select songs", "Check for arrival confirmations",
                "Make seating arrangements", "Immerse in the mikvah"};

        String desc[] = {"Prepare an organized list of all guests.",
                "Choosing the place where the event will take place (event hall / garden) and the date.",
                "A makeup artist and a hair designer for the bride who arrives at the place you are getting ready on the wedding day.",
                "photographers - video and stills.",
                "Choosing a DJ for a wedding.",
                "Choosing a rabbi for a chuppah.",
                "Registration for marriage at any marriage bureau in Israel.",
                "Choose the dress you will wear on your wedding day.",
                "Choose the outfit you will wear on your wedding day.",
                "Lessons on family purity and what is required from the bride.",
                "Wedding rings for bride and groom.",
                "For couples who do not own a car or want to rent a luxury car for the wedding day.",
                "Choosing a printing house where you will design and print your wedding invitations.",
                "Choose the shoes you will wear on your wedding day.",
                "Choose accessories.",
                "Getting ready for the wedding can take place in a hotel room, bridal salon and even at home.",
                "Distribution of invitations to wedding guests.",
                "Choosing a bridal bouquet for filming and for the chuppah.",
                "Choosing songs for the chuppah, breaking the glass and for the first dance of the bride and groom.",
                "Send messages for confirmations of arrival.",
                "Setting seats for the guests",
                "Immersion in the mikvah is required from all brides who marry in the framework of the rabbinate."};

        String tips[] = {"1. A list of invited guests must be prepared, including name, last name, number of guests and telephone number.\n" +
                "2. Keep in mind that the number of guests on the list is not the actual number of guests that will arrive, and it is expected that about 20% of them will not arrive. This should be taken into account when you required for a minimum number of guests in the contract with the hall.",

                "1. Check for a valid business license.\n" +
                        "2. It is important to understand what is included in the price of a dish - whether it is only a dish or that includes lighting and amplification, bar services and more.\n" +
                        "3. It is recommended to check for accessibility for people with disabilities.\n" +
                        "4. It is recommended to make sure there is enough parking nearby and there is no additional charge.\n" +
                        "5. This is the first supplier to be hired. Only after signing a contract with the hall, there is a definite date and you can start looking for other suppliers.",

                "1. Take a look at previous works and get recommendations.\n" +
                        "2. Introductory meeting / trial before to get to know a little before the wedding day. They have a great impact on how you look on your wedding day, you should feel comfortable  with them.\n" +
                        "3. Set the arrival hours so that you will be ready to photo shoot on time. \n" +
                        "4. It is recommended to do hair before makeup.",

                "1. Look at photos and works by photographers online. Look for recommendations.\n" +
                        "2. Make appointments with some photographers you loved their work. The dynamics with them is very important since they will accompany you throughout the day.",

                "1. Search for recommendations.\n" +
                        "2. Go see them on action during an event.\n" +
                        "3. Make an appointment and get an impression. DJ has an important role to play in the event and they can have a great impact on the success of it.\n" +
                        "4. Usually through the DJ you can take a deals for magnets and effects for the first dance of the bride and groom.",

                "1. Look for a rabbi that suits you. For example, there are those who like a singing rabbi, some like a short and quick chuppah. \n" +
                        "2. There are rabbis who come free of charge as part of a mitzvah and some take a fee.",

                "1. Come prepared - check what documents must be presented when registering and what to bring according to the bureau you intend to attend. Each bureau has its own procedures - some religious councils will ask you to present additional documents. Most of the bureaus can be reached at reception hours without prior notice, and some are required to coordinate arrival. It is necessary to bring 2 witnesses who meet the requirements of the rabbinate.\n" +
                        "2. Opening case involves payment, but there are discounts for certain populations - see if you are eligible.",

                "1. It is recommended to look online for favourit styles before meetings and have an idea of what style you like.\n" +
                        "2. You may want to try dresses at some different designers.\n" +
                        "3. Consider that several measurements must be made at different times to match the dress to the body. It is best to make a final measurement as close as possible to the date of the wedding in case further changes are required.",

                "1. Choose a comfortable suit with which you will feel comfortable all day long.\n" +
                        "2. Bring a second comfortable T shirt for the dancing part.",

                "1. The Rabbinate directs the bride to the Rebbetzin. The training is free of charge.\n" +
                        "2. The duration of the training is approximately two hours. Usually one meeting, sometimes two.\n" +
                        "3. At the end of the training, the Rebbetzin gives the bride confirmation that she has undergone a \"bride's guidance\", which is attached to the marriage file that must be presented to the rabbi at the wedding ceremony.",

                "1. A wedding ring should be round and complete. Engraving is allowed. It is forbidden to sanctify with an inlaid ring, but you can always choose a ring that you can inlaid after the wedding.\n" +
                        "2. The ring should be the property of the man, meaning that he purchased it from his own money and finished paying for it before the wedding.\n" +
                        "3. A man's wedding ring is not a halachic custom and it is not obligatory to buy a ring for the groom.",

                "1. It is recommended to choose a driver who you feel comfortable with, and you wish to accompany you throughout the day.\n" +
                        "2. Take care of the driver's insurance.\n" +
                        "3. Pay attention to the collecting and returning hours of the vehicle.",

                "1. This is an expense that looks small but can be huge. There are printing houses that offer attractive deals and a market survey is recommended.\n" +
                        "2. Check all the details in the sketch before confirming the printing to avoid inconvenience with the printer / guests.\n" +
                        "3. Consider the amount of invitations you want to print, so you don't pay for unnecessarily large amounts.",

                "1. Remember that these shoes are worn all day long - during the photoshoot before the wedding, the chuppah and dancing. Choose comfortable shoes.\n" +
                        "2. If you have chosen heels, you may want to bring a pair of flat shoes to change.",

                "If you are interested in a hair ornament, you must choose a hairstyle first.",

                "1. There is a need for a spacious room that will be able to contain -makeup artist, hair stylist, photographers, bride and escorts. Sometimes even a separate makeup artist and hair stylist for the escorts.\n" +
                        "2. Make sure you have suitable chairs, enough mirrors and electrical power divider.",

                "1. Take into consideration that this process takes a long time and can't be completed in one day. Especially because you depend on the schedules of the invitees.\n" +
                        "2. If you need to send invitations by the post office or even abroad, take into consideration that it may take time and make sure that the invitations have reached their destination.",

                "Usually the bouquet is collected on the day of the wedding (to make sure the flowers are fresh). During this stressfull day, many times the couple forgets to pick it up.",

                "1. The DJ will ask you to choose the songs for these moments during the wedding.\n" +
                        "2. Sometimes the couples even choose other songs they want to be played during the party.",

                "1. Must be made after all the invitations have been distributed.\n" +
                        "2. It is recommended to perform two rounds of SMS messages. The messages must be at least two days apart in order to give the guests time to respond and prevent harassment.",

                "1. Seating arrangements can be made only after the completion of the guests arrivals confimation.\n" +
                        "2. Only those who have confirmed arrival must be considered. The invitees who did not answer should not be considered.",

                "1. The immersion should be made as close as possible to the wedding day, preferably up to three days before the maximum.\n" +
                        "2. An immersion date and time must be set i accordancewith the chosen mikvah.\n" +
                        "4. immersion involves payment.\n" +
                        "5. At the end, a confirmation of the immersion is given to the bride, which is attached to the marriage file that must be presented to the rabbi at the wedding ceremony."};

        String endDate[] = {"as soon as possible.", "as soon as possible.", "6 month before the wedding.", "6 month before the wedding.", "6 month before the wedding.",
                "4 month before the wedding.", "At the latest, up to 3 months before the wedding.", "3 month before the wedding.", "3 month before the wedding.",
                "2-3 month before the wedding.", "2-3 month before the wedding.", "2-3 month before the wedding.", "2-3 month before the wedding.",
                "1-2 month before the wedding.", "1-2 month before the wedding.", "1-2 month before the wedding.", "1-2 month before the wedding.",
                "1 month before the wedding.", "1 month before the wedding.", "1 month before the wedding.", "1 month before the wedding.", "1-3 days before the wedding."};

        String estimatedPrice[] = {"----", "180-450 per serving", "m.a 700-2000 hair 600-3500", "8000-16000", "3500-6500", "0-2000", "420-1200", "1500-15000",
                "1000-35000", "----", "300-1200", "1000-2000", "0.8-20 per invitation", "150-600", "120-500", "700-3500 for hotel", "----", "100-200", "----", "----", "----", "15-35"};


        for (int i = 0; i < Constants.TO_DO_TASKS; i++) {
            TaskClass task = new TaskClass(taskName[i]);
            task.setmID(i + "");
            task.setmDescription(desc[i]);
            task.setmTips(tips[i]);
            task.setmEndDate(endDate[i]);
            task.setmEstimatedPrice(estimatedPrice[i]);
            task.setmIsDone("FALSE");

            ConnectionToFirebase.getInstance().enterDataToDB(task, Constants.TASKS); // insert task to firebase
        }

    }

    @Override
    public void initializeViews() {
        mView.initializeViews(mTaskClassList);
    }

    @Override
    public void removeFromDB(String getmID) {
        FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.TASKS_DB_URL).child(getmID).removeValue(); // remove from Firebase.
    }

    @Override
    public void onItemSelected(int positionFilter) {
        ArrayList<TaskClass> filteredTasks;

        switch (positionFilter) {
            case 0: // All tasks
                filteredTasks = new ArrayList<>(mTaskClassList);
                break;
            case 1: // done
                filteredTasks = filter("true");
                break;
            case 2: // not done
                filteredTasks = filter("false");
                break;
            default:
                filteredTasks = new ArrayList<>(mTaskClassList);

        }

        mView.updateAdapter(filteredTasks);
    }

    private ArrayList<TaskClass> filter(String isDone) {
        // create new list to show according to filter
        ArrayList<TaskClass> tasksList = new ArrayList<>();
        for (TaskClass task : mTaskClassList) {
            if (task.getmIsDone().equalsIgnoreCase(isDone)) {
                tasksList.add(task);
            }
        }
        return tasksList;
    }

    private void getAllDetailsFromDB() {
        // read data from db
        ConnectionToFirebase.getInstance().getAllTasksDetailsFromDB(taskList -> {
            mTaskClassList = new ArrayList<TaskClass>(taskList);
            mView.updateAdapter(taskList); // update list view

        });

    }

}
