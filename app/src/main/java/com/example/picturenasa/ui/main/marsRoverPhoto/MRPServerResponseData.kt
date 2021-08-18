package com.example.picturenasa.ui.main.marsRoverPhoto

import com.google.gson.annotations.SerializedName

class MRPServerResponseData (
    @field:SerializedName("photos") val photos: List<RoversData>?
)

data class RoversData (
        val id: Int,
        val sol: Int,
        val camera: RoversCamera,
        val img_src: String,
        val earth_date: String,
        val rover: RoversID)

data class RoversCamera(
        val id: Int,
        val name: String,
        val rover_id: Int,
        val full_name: String)

data class RoversID (
        val id: Int,
        val name: String,
        val landing_date: String,
        val launch_date: String,
        val status: String)
