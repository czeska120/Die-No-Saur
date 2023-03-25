package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models

import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R

class DinoSprite(
    name: String,
    walk: Int,
    run: Int,
    hit: Int,
    dead: Int
) {

}

var Dinos = arrayListOf<DinoSprite>(
    DinoSprite("Nico", R.drawable.nico, R.drawable.nico_run, R.drawable.nico_hit, R.drawable.nico_run),
    DinoSprite("Doux", R.drawable.doux, R.drawable.doux_run, R.drawable.doux_hit, R.drawable.doux_dead),
    DinoSprite("Kira", R.drawable.kira, R.drawable.kira_run, R.drawable.kira_hit, R.drawable.kira_dead),
    DinoSprite("Kuro", R.drawable.kuro, R.drawable.kuro_run, R.drawable.kuro_hit, R.drawable.kuro_dead),
    DinoSprite("Mono", R.drawable.mono, R.drawable.mono_run, R.drawable.mono_hit, R.drawable.mono_dead),
    DinoSprite("Olaf", R.drawable.olaf, R.drawable.olaf_run, R.drawable.olaf_hit, R.drawable.olaf_dead)
)