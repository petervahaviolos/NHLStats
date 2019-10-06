package com.example.myfirstapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ahmadrosid.svgloader.SvgLoader
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_team_stats.*
import kotlinx.android.synthetic.main.content_team_stats.*

class TeamStatsActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_stats)

        linearLayoutManager = LinearLayoutManager(this)
        requestQueue = Volley.newRequestQueue(this)

        val teamId: Int = intent.getIntExtra("teamId", 0)

        requestStats(teamId)
        downloadTeamLogo(teamId)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.content_team_stats, container, false)

    private fun requestStats(teamId: Int) {
        val url = "https://statsapi.web.nhl.com/api/v1/teams/${teamId}"
        val team = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val teamData = response.getJSONArray("teams")
                val teamInfo = teamData.getJSONObject(teamData.length() - 1)
                val teamName = teamInfo.getString("name")
                textViewTeamName.append(teamName)
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

    override fun onDestroy() {
        super.onDestroy()
        SvgLoader.pluck().close()
    }
}
