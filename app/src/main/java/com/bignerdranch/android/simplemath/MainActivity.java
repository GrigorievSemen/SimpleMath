package com.bignerdranch.android.simplemath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {

    private static final String TAG = "QuizActivity";//для записи сообщений программы

    private EditText NamePersonEditText;
    private Spinner changeSpinner;
    private Spinner change_PersonSpinner;
    private TextView writeNameTextView;// имя ученика которое пишем и добавляе в Спиннер change_PersonSpinner
    private TextView nameTextView; //  имя ученика которое показывается текущим
    private TextView TextViewShow_progress; //  прогресс ученика
    private Button addButton;
    private Button addPersonButtom;
    private Button deletePersonButtom;
    private Button yesButtom;
    private Button noButtom;
    private String change;//  текст который остался в Spinner changeSpinner
    private String namePerson = "ВЫБЕРИТЕ УЧЕНИКА";

    //   private List<String> personList = new ArrayList<>();//лист с именами
    private List<Person> PersonList = new ArrayList<>();//лист с прогрессом учеников


    private final static String FILE_NAME = "saveNamesPerson.txt";// имя файла сохранения имен
    private final static String FILE_PERSON = "savePerson.txt";// имя файла сохранения Учеников с прогрессом


    @Override
    protected void onRestart() {
        super.onRestart();
        showProgress(String.valueOf((changeSpinner.getSelectedItem())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");//включение команды регистрации сообщения
        setContentView(R.layout.activity_main);

        NamePersonEditText = (EditText) findViewById(R.id.namePerson_edit);
        changeSpinner = (Spinner) findViewById(R.id.change);
        change_PersonSpinner = (Spinner) findViewById(R.id.change_Person);
        writeNameTextView = (TextView) findViewById(R.id.write_name);
        TextViewShow_progress = (TextView) findViewById(R.id.show_progress);
        nameTextView = (TextView) findViewById(R.id.name);
        addButton = (Button) findViewById(R.id.add);
        addPersonButtom = (Button) findViewById(R.id.addPerson);
        deletePersonButtom = (Button) findViewById(R.id.deletePerson);
        yesButtom = (Button) findViewById(R.id.yes);
        noButtom = (Button) findViewById(R.id.no);

        NamePersonEditText.setVisibility(View.INVISIBLE);
        writeNameTextView.setVisibility(View.INVISIBLE);
        nameTextView.setVisibility(View.INVISIBLE);
        addPersonButtom.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.INVISIBLE);
        yesButtom.setVisibility(View.INVISIBLE);
        noButtom.setVisibility(View.INVISIBLE);


        // personList = deserializePerson();//загружаем данные если они есть
        deserializePersonList();


//       personList.clear();
//      PersonList.clear();
//       serializePerson(personList);
//       serializePersonList(PersonList);


        SpinnerChangePerson(PersonList);//метод добавляет в спинер выбора учеников учеников


        showProgress(String.valueOf((changeSpinner.getSelectedItem())));


        changeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {// обработка нажание на Спинер с выбором режима
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                String str = String.valueOf((changeSpinner.getSelectedItem()));
                showProgress(str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        change_PersonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {// обработка нажание на Спинер с выбором ученика
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String str = String.valueOf((change_PersonSpinner.getSelectedItem()));
                String str1 = String.valueOf((changeSpinner.getSelectedItem()));

                if (!str.equals("ВЫБЕРИТЕ УЧЕНИКА")) {
                    nameTextView.setText(str);
                    namePerson = nameTextView.getText().toString();
                    nameTextView.setVisibility(View.VISIBLE);
                    change_PersonSpinner.setSelection(0);
                    showProgress(str1);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void showProgress(String str) {// метод показывает прогресс ученика
        TextViewShow_progress.setVisibility(View.VISIBLE);
        deserializePersonList();
        int numberPerson = 0;
        for (int i = 0; i < PersonList.size(); i++) {
            if (PersonList.get(i).getNamePerson().equals(nameTextView.getText().toString())) {
                numberPerson = i;
            }
        }
        String str1 = nameTextView.getText().toString();
        if (!str1.equals("ВЫБЕРИТЕ УЧЕНИКА") && !str1.equals("") && !str.equals("ВЫБРАТЬ РЕЖИМ")) {
            switch (str) {
                case "Режим умножения":
                    TextViewShow_progress.setText("Статистика ответов:\n" + "правильных - " + PersonList.get(numberPerson).getMultiplicationGood() + "\n" + "неправильных - " + PersonList.get(numberPerson).getMultiplicationBad());
                    break;
                case "Режим деление":
                    TextViewShow_progress.setText("Статистика ответов:\n" + "правильных - " + PersonList.get(numberPerson).getDivideGood() + "\n" + "неправильных - " + PersonList.get(numberPerson).getDivideBad());
                    break;
            }
        }
    }


    public void serializePersonList(List<Person> list) {
        try {
            FileOutputStream outputStreamPerson = openFileOutput(FILE_PERSON, Context.MODE_PRIVATE);
            ObjectOutputStream oosPerson = new ObjectOutputStream(outputStreamPerson);
            oosPerson.writeObject(list);
            oosPerson.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deserializePersonList() {

        try {
            FileInputStream fisPerson = openFileInput(FILE_PERSON);
            if (fisPerson != null) {
                ObjectInputStream oisPerson = new ObjectInputStream(fisPerson);
                PersonList = (List<Person>) oisPerson.readObject();
                oisPerson.close();
                for (int i = 0; i < PersonList.size(); i++) {// удаляем пустые строки
                    String str = PersonList.get(i).getNamePerson().replaceAll("\\s", "");
                    if (str.equals("")) PersonList.remove(i);
                }
                if (PersonList.size() == 0)
                    PersonList.add(0, new Person("ВЫБЕРИТЕ УЧЕНИКА", 0, 0, 0, 0));
            }
        } catch (Exception e) {
        }
    }


    public void SpinnerChangePerson(List<Person> listPerson) {//добавляем в Spinner учеников
        List<String> list = new ArrayList<>();
        for (int i = 0; i < listPerson.size(); i++) {
            list.add(listPerson.get(i).getNamePerson());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        change_PersonSpinner.setAdapter(adapter);
    }

    public void addPerson(View view) {//обработка кнопки добавит ученика
        NamePersonEditText.setVisibility(View.VISIBLE);
        writeNameTextView.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.VISIBLE);
        addPersonButtom.setVisibility(View.INVISIBLE);
        deletePersonButtom.setVisibility(View.INVISIBLE);
    }


    public void add(View view) {//обработка нажатии кнопки добавить
        String str = NamePersonEditText.getText().toString().replaceAll(" +", "");
        List<String> list = new ArrayList<>();
        for (int i = 0; i < PersonList.size(); i++) {
            list.add(PersonList.get(i).getNamePerson());
        }

        NamePersonEditText.setVisibility(View.INVISIBLE);
        writeNameTextView.setVisibility(View.INVISIBLE);

        if (PersonList.size() == 9) {
            Toast toast = Toast.makeText(MainActivity.this, R.string.full_list, Toast.LENGTH_LONG);// выводим текст Учеников не может быть больше 8!
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

            NamePersonEditText.setText(null);
            addPersonButtom.setVisibility(View.VISIBLE);
            deletePersonButtom.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);

        } else if (list.contains(str)) {// проверка на одинаковые имена
            Toast toast = Toast.makeText(MainActivity.this, R.string.name_exists, Toast.LENGTH_LONG);// выводим текст Такое имя уже есть!
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

            nameTextView.setVisibility(View.VISIBLE);
            deletePersonButtom.setVisibility(View.VISIBLE);
            addPersonButtom.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            NamePersonEditText.setText("");
        } else {
            PersonList.add(createPersonList(NamePersonEditText.getText().toString()));//добавляем нового ученика

            nameTextView.setVisibility(View.VISIBLE);
            deletePersonButtom.setVisibility(View.VISIBLE);
            addPersonButtom.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            NamePersonEditText.setText("");

            try {
                serializePersonList(PersonList);
                deserializePersonList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            SpinnerChangePerson(PersonList);
        }
    }


    public void deletePerson(View view) {//обработка нажатии кнопки удалить ученика

        if (namePerson.equals("ВЫБЕРИТЕ УЧЕНИКА") || namePerson.equals("")) {
            Toast toast = Toast.makeText(MainActivity.this, R.string.must_select_study, Toast.LENGTH_LONG);// выводим текст Необходимо выбрать ученика!
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } else {
            yesButtom.setVisibility(View.VISIBLE);
            noButtom.setVisibility(View.VISIBLE);
            deletePersonButtom.setVisibility(View.INVISIBLE);
            addPersonButtom.setVisibility(View.INVISIBLE);
            namePerson = nameTextView.getText().toString();// запоминаем имя, так как оно понадобиться в методе yes
            nameTextView.setText("Удалить ученика " + namePerson + "?");
        }
    }

    public void yes(View view) {//обработка нажатии кнопки да

        for (int i = 0; i < PersonList.size(); i++) {
            if (PersonList.get(i).getNamePerson().equals(namePerson)) {
                PersonList.remove(i);
            }
        }

        serializePersonList(PersonList);
        deserializePersonList();
        SpinnerChangePerson(PersonList);

        nameTextView.setText("");
        namePerson = "";

        yesButtom.setVisibility(View.INVISIBLE);
        noButtom.setVisibility(View.INVISIBLE);
        deletePersonButtom.setVisibility(View.VISIBLE);
        TextViewShow_progress.setVisibility(View.INVISIBLE);
        addPersonButtom.setVisibility(View.VISIBLE);
    }

    public void no(View view) {//обработка нажатии кнопки нет
        yesButtom.setVisibility(View.INVISIBLE);
        noButtom.setVisibility(View.INVISIBLE);
        deletePersonButtom.setVisibility(View.VISIBLE);
        addPersonButtom.setVisibility(View.VISIBLE);
        nameTextView.setText(namePerson);


    }

    public Person createPersonList(String str) {
        return new Person(str, 0, 0, 0, 0);
    }


    public void start(View viev) {//обработка кнопки старт

        Intent intent_multiplication = new Intent(MainActivity.this, Multiplication.class);//создаем экземляр класса для перехода
        intent_multiplication.putExtra("NumberPerson", namePerson); // переносим в другое активити номер в массиве текущего ученика

        Intent intent_divide = new Intent(MainActivity.this, Divide.class);//создаем экземляр класса для перехода
        intent_divide.putExtra("NumberPerson", namePerson); // переносим в другое активити номер в массиве текущего ученика


        change = String.valueOf(changeSpinner.getSelectedItem());
        if (change.equals("Режим умножения") && !namePerson.equals("ВЫБЕРИТЕ УЧЕНИКА") && !namePerson.equals("")) {
            startActivity(intent_multiplication);
        } else if (change.equals("Режим деление") && !namePerson.equals("ВЫБЕРИТЕ УЧЕНИКА") && !namePerson.equals("")) {
            startActivity(intent_divide);
        } else {
            Toast toast = Toast.makeText(MainActivity.this, R.string.must_select_mode, Toast.LENGTH_LONG);// выводим текст Необходимо выбрать режим и ученика!
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
    }
}

