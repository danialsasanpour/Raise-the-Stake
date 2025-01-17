package com.example.raisethestake;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import model.Match;
import model.Player;

public class SubmitResults extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmitResult;
    ImageButton btnHome, btnPlayerSearch, btnDashboard;
    TextView tvUsername, tvBalance, tvPlayer1, tvPlayer2;
    ImageView imgProfilePicture;

    Match currentMatch;
    Player currentPlayer;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference matches = root.getReference("Matches");
    DatabaseReference players = root.getReference("Players");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_results);

        initialize();
    }

    private void initialize()
    {
        currentPlayer = (Player) getIntent().getExtras().getSerializable("currentPlayer");
        currentMatch = (Match) getIntent().getExtras().getSerializable("currentMatch");

        btnHome = findViewById(R.id.btnHome);
        btnPlayerSearch = findViewById(R.id.btnPlayerSearch);
        btnDashboard = findViewById(R.id.btnDashboard);
        tvUsername = findViewById(R.id.tvUsername);
        tvBalance = findViewById(R.id.tvBalance);
        tvUsername.setText(currentPlayer.getUsername());
        tvBalance.setText(String.valueOf(currentPlayer.getBalance()));
        imgProfilePicture = findViewById(R.id.imgProfilePicture);
        tvPlayer1 = findViewById(R.id.tvPlayer1);
        tvPlayer2 = findViewById(R.id.tvPlayer2);
        tvPlayer1.setText(currentMatch.getPlayer1());
        tvPlayer2.setText(currentMatch.getPlayer2());
        if (currentPlayer.getProfilePicture() != null)
        {
            Picasso.with(this).load(currentPlayer.getProfilePicture()).into(imgProfilePicture);
        }

        btnSubmitResult = findViewById(R.id.buttonSubmit);
        btnSubmitResult.setOnClickListener(this);


        matches.child(currentMatch.getUuid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                /*
                if (currentMatch.getPlayer1().equals(currentPlayer.getUsername()))
                {
                    String playerWon1 = snapshot.getValue(String.class);
                    if (currentMatch.getPlayerWon2() != null)
                    {
                        if (!playerWon1.equals(currentMatch.getPlayerWon2()))
                            Toast.makeText(SubmitResults.this, "Results from the 2 players are not the same", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    String playerWon2 = snapshot.getValue(String.class);
                    if (currentMatch.getPlayerWon1() != null)
                    {
                        if (!playerWon2.equals(currentMatch.getPlayerWon1()))
                            Toast.makeText(SubmitResults.this, "Results from the 2 players are not the same", Toast.LENGTH_LONG).show();
                    }
                }

*/
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        Intent intent;
        switch (id)
        {
            case R.id.buttonSubmit:
                submitResult(view);
                break;
            case R.id.btnHome:
                intent = new Intent(this, Home.class);
                intent.putExtra("currentPlayer", currentPlayer);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
                break;
            case R.id.btnPlayerSearch:
                intent = new Intent(this, SearchForPlayer.class);
                intent.putExtra("currentPlayer", currentPlayer);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
                break;
            case R.id.btnDashboard:
                intent = new Intent(this, account.class);
                intent.putExtra("currentPlayer", currentPlayer);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intent);
                break;
        }
    }

    private void submitResult(View view)
    {
        final Dialog dialog = new Dialog(SubmitResults.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);

        dialog.setContentView(R.layout.layout_submitresult);


        final EditText edPlayer1Score, edPlayer2Score;
        edPlayer1Score = dialog.findViewById(R.id.edPlayer1Score);
        edPlayer2Score = dialog.findViewById(R.id.edPLayer2Score);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int player1Score = Integer.valueOf(edPlayer1Score.getText().toString());
                int player2Score = Integer.valueOf(edPlayer2Score.getText().toString());
                String playerWon = "";

                if (player1Score > player2Score)
                    playerWon = currentMatch.getPlayer1();
                else if (player2Score > player1Score)
                    playerWon = currentMatch.getPlayer2();
                else
                    Toast.makeText(SubmitResults.this, "Invalid Results", Toast.LENGTH_LONG).show();

                if (currentMatch.getPlayer1().equals(currentPlayer.getUsername()))
                {
                    currentMatch.setPlayerWon1(playerWon);
                }
                else
                {
                    currentMatch.setPlayerWon2(playerWon);
                }

                matches.child(currentMatch.getUuid()).setValue(currentMatch);

            }
        });

        dialog.show();
    }

}