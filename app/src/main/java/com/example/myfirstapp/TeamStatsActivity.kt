package com.example.myfirstapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.ahmadrosid.svgloader.SvgLoader
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_team_stats.*

class TeamStatsActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestQueue: RequestQueue
    private lateinit var players: MutableList<Player>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_stats)

        linearLayoutManager = LinearLayoutManager(this)
        requestQueue = Volley.newRequestQueue(this)

        val teamId: Int = intent.getIntExtra("teamId", 0)
        players = mutableListOf()
        downloadTeamInfo(teamId)
        //downloadTeamLogo(teamId)

    }

    private fun downloadTeamInfo(teamId: Int) {
        val url = "https://statsapi.web.nhl.com/api/v1/teams/${teamId}/?expand=team.roster"
        val team = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val teamData = response.getJSONArray("teams")
                val teamInfo = teamData.getJSONObject(teamData.length() - 1)

                val teamRoster = teamInfo.getJSONObject("roster").getJSONArray("roster")
                for (i in 0 until teamRoster.length() - 1) {
                    Log.d("downloadTeamInfo", "Downloading info for player $i")
                    val playerInfo = teamRoster.getJSONObject(i).getJSONObject("person")
                    val playerPositionInfo = teamRoster.getJSONObject(i).getJSONObject("position")

                    val playerId = playerInfo.getInt("id")
                    val playerName = playerInfo.getString("fullName")
                    val playerPosition = playerPositionInfo.getString("name")
                    val playerPicture = Picasso.get().load("https://nhl.bamcontent.com/images/headshots/current/168x168/${playerId}.jpg")

                    players.add(Player(playerId, playerName, playerPosition, playerPicture))
                }
                Log.d("downloadTeamInfo", "${players.count()} players downloaded. ")
                Log.d("downloadTeamInfo", "${players.get(0).name}")

                recyclerViewTeamRoster.apply {
                    Log.d("TeamStatsActivity", "${players.size} - length of player array")
                    layoutManager = linearLayoutManager
                    adapter = PlayerAdapter(players)
                }
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }
        )
        requestQueue.add(team)

    }

    private fun downloadTeamLogo(teamId: Int) {
        val url =
            "https://www-league.nhlstatic.com/images/logos/teams-current-primary-light/${teamId}.svg"
        SvgLoader.pluck()
            .with(this)
            .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
            .load(url, imageViewTeamLogo)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        SvgLoader.pluck().close()
//    }
}
