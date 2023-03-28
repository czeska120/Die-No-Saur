package ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.models

import ph.edu.dlsu.mobdeve.ang.silvestre.dienosaur.R

class DinoSprite(
    val name: String,
    val walk: Int,
    val run: Array<Int>,
    val hit: Array<Int>,
    val dead: Int
) {
    var runFrame: Int = 0
    var hitFrame: Int = 0
}

// run sprites
private val nico_run = arrayOf<Int>(R.drawable.nico_run_1, R.drawable.nico_run_2, R.drawable.nico_run_3, R.drawable.nico_run_4, R.drawable.nico_run_5, R.drawable.nico_run_6)
private val doux_run = arrayOf<Int>(R.drawable.doux_run_1, R.drawable.doux_run_2, R.drawable.doux_run_3, R.drawable.doux_run_4, R.drawable.doux_run_5, R.drawable.doux_run_6)
private val kira_run = arrayOf<Int>(R.drawable.kira_run_1, R.drawable.kira_run_2, R.drawable.kira_run_3, R.drawable.kira_run_4, R.drawable.kira_run_5, R.drawable.kira_run_6)
private val kuro_run = arrayOf<Int>(R.drawable.kuro_run_1, R.drawable.kuro_run_2, R.drawable.kuro_run_3, R.drawable.kuro_run_4, R.drawable.kuro_run_5, R.drawable.kuro_run_6)
private val mono_run = arrayOf<Int>(R.drawable.mono_run_1, R.drawable.mono_run_2, R.drawable.mono_run_3, R.drawable.mono_run_4, R.drawable.mono_run_5, R.drawable.mono_run_6)
private val olaf_run = arrayOf<Int>(R.drawable.olaf_run_1, R.drawable.olaf_run_2, R.drawable.olaf_run_3, R.drawable.olaf_run_4, R.drawable.olaf_run_5, R.drawable.olaf_run_6)

// hit sprites
private val nico_hit = arrayOf<Int>(R.drawable.nico_hit_1, R.drawable.nico_hit_2, R.drawable.nico_hit_3, R.drawable.nico_hit_4)
private val doux_hit = arrayOf<Int>(R.drawable.doux_hit_1, R.drawable.doux_hit_2, R.drawable.doux_hit_3, R.drawable.doux_hit_4)
private val kira_hit = arrayOf<Int>(R.drawable.kira_hit_1, R.drawable.kira_hit_2, R.drawable.kira_hit_3, R.drawable.kira_hit_4)
private val kuro_hit = arrayOf<Int>(R.drawable.kuro_hit_1, R.drawable.kuro_hit_2, R.drawable.kuro_hit_3, R.drawable.kuro_hit_4)
private val mono_hit = arrayOf<Int>(R.drawable.mono_hit_1, R.drawable.mono_hit_2, R.drawable.mono_hit_3, R.drawable.mono_hit_4)
private val olaf_hit = arrayOf<Int>(R.drawable.olaf_hit_1, R.drawable.olaf_hit_2, R.drawable.olaf_hit_3, R.drawable.olaf_hit_4)

val Dinos = arrayListOf<DinoSprite>(
    DinoSprite("Nico", R.drawable.nico, nico_run, nico_hit, R.drawable.nico_dead),
    DinoSprite("Doux", R.drawable.doux, doux_run, doux_hit, R.drawable.doux_dead),
    DinoSprite("Kira", R.drawable.kira, kira_run, kira_hit, R.drawable.kira_dead),
    DinoSprite("Kuro", R.drawable.kuro, kuro_run, kuro_hit, R.drawable.kuro_dead),
    DinoSprite("Mono", R.drawable.mono, mono_run, mono_hit, R.drawable.mono_dead),
    DinoSprite("Olaf", R.drawable.olaf, olaf_run, olaf_hit, R.drawable.olaf_dead)
)