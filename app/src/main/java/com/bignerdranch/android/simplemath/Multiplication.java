package com.bignerdranch.android.simplemath;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Multiplication extends AppCompatActivity {
    private static int a, b;
    private static int correct;
    private static int incorrect;


    private List<Person> PersonList = new ArrayList<>();//лист с прогрессом учеников
    private final static String FILE_PERSON = "savePerson.txt";// имя файла сохранения Учеников с прогрессом
    private String namePerson = "";  // имя активного ученика в массиве учеников
    private int numberPerson = 0; // номер активного ученика в массиве игроков


    private TextView ExampleTextView;//текст примера
    private TextView CorrectTextView;// текст кол-во правильных ответов
    private TextView IncorrectTextView;// текст кол-во не правильных ответов
    private TextView MessageResultTextView;// текст показывает правильный ответ
    private EditText AnswerEditText; // ввод ответа
    private Button CheckButton;
    private Button NextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplication);


        namePerson = getIntent().getExtras().getString("NumberPerson");
        PersonList = deserializePersonList();
        for (int i = 0; i < PersonList.size(); i++) {
            if (PersonList.get(i).getNamePerson().equals(namePerson)) {
                numberPerson = i;
            }
        }


        correct = 0;
        incorrect = 0;


        AnswerEditText = (EditText) findViewById(R.id.answer);
        ExampleTextView = (TextView) findViewById(R.id.example);
        CorrectTextView = (TextView) findViewById(R.id.correct);
        IncorrectTextView = (TextView) findViewById(R.id.incorrect);
        MessageResultTextView = (TextView) findViewById(R.id.message_result);
        CheckButton = (Button) findViewById(R.id.check_button);
        NextButton = (Button) findViewById(R.id.next_button);
        NextButton.setVisibility(View.GONE);

        turnOnOffKeyboard(true);//включаем клавиатуру

        exampleShow();

        AnswerEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {//обработка нажатие ENTER как аналог CheckButton
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (isEmpty(AnswerEditText)) {//если поле с ответом пустое
                        null_answer();//выводим сообщение
                    } else {
                        check(Integer.parseInt(AnswerEditText.getText().toString()));
                    }
                    return true;
                }
                return false;
            }
        });
    }


    public void checkButton(View view) {
        if (isEmpty(AnswerEditText)) {//если поле с ответом пустое
            null_answer();//выводим сообщение
        } else {
            check(Integer.parseInt(AnswerEditText.getText().toString()));
        }
    }

    public void nextButton(View view) {
        turnOnOffKeyboard(true);//включаем клавиатуру

        NextButton.setVisibility(View.GONE);
        MessageResultTextView.setVisibility(View.GONE);
        ExampleTextView.setVisibility(View.VISIBLE);
        AnswerEditText.setVisibility(View.VISIBLE);
        CheckButton.setVisibility(View.VISIBLE);
        exampleShow();
    }

    public void exampleShow() {//вывод примера в виджет
        a = (int) (1 + Math.random() * 12);
        b = (int) (1 + Math.random() * 12);
        ExampleTextView.setText(a + " x " + b + "  =");

    }

    public void check(int check) {//проверка введенного ответа

        if (check == a * b) {
            correct++;

            int i = PersonList.get(numberPerson).getMultiplicationGood();
            PersonList.get(numberPerson).setMultiplicationGood(++i);


            Toast toast = Toast.makeText(this, R.string.correct, Toast.LENGTH_SHORT);// выводим текст Правильно!!
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } else {
            turnOnOffKeyboard(false);//выключаем клавиатуру
            incorrect++;

            int i = PersonList.get(numberPerson).getMultiplicationBad();
            PersonList.get(numberPerson).setMultiplicationBad(++i);


            Toast toast = Toast.makeText(this, R.string.incorrect, Toast.LENGTH_LONG);// выводим текст Неверно!!
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            ExampleTextView.setVisibility(View.GONE);
            AnswerEditText.setVisibility(View.GONE);
            CheckButton.setVisibility(View.GONE);
            MessageResultTextView.setVisibility(View.VISIBLE);
            MessageResultTextView.setText("Правильный ответ = " + a * b);
            NextButton.setVisibility(View.VISIBLE);

        }

        serializePersonList(PersonList);
        PersonList = deserializePersonList();


        IncorrectTextView.setText(Integer.toString(incorrect));
        CorrectTextView.setText(Integer.toString(correct));
        AnswerEditText.setText(null);
        exampleShow();
        //обнуляем поле

    }


    protected boolean isEmpty(EditText editText) {//проверка на пустое/заполненное поле
        return (editText.getText().toString().equals(""));
    }


    public void null_answer() {//при пустом поле ответа выводим сообщение
        Toast toast = Toast.makeText(this, R.string.null_name, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
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

    public List<Person> deserializePersonList() {
        List<Person> person = new ArrayList<>();
        try {
            FileInputStream fisPerson = openFileInput(FILE_PERSON);
            if (fisPerson != null) {
                ObjectInputStream oisPerson = new ObjectInputStream(fisPerson);
                person = (List<Person>) oisPerson.readObject();
                oisPerson.close();
                return person;
            }

        } catch (Exception e) {

        }
        return person;
    }

    public void turnOnOffKeyboard(boolean b) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (b == true) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);//сразу выводит клавиатуру в активности
        } else {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);//выключает клавиатуру в активности
        }
    }


}
