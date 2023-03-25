package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models

import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R

class BG (
    name: String,
    dark: Int,
    top: Int,
    bottom: Int
){

}

var BGs = arrayListOf<BG>(
    BG("Fall", R.drawable.dark_fall, R.drawable.bg_falltop, R.drawable.bg_fallbot),
    BG("Spring", R.drawable.dark_spring, R.drawable.bg_springtop, R.drawable.bg_springbot),
    BG("Winter", R.drawable.dark_winter, R.drawable.bg_wintertop, R.drawable.bg_winterbot)
)