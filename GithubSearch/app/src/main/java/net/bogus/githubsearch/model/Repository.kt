package net.bogus.githubsearch.model

/**
 * Created by burak on 11/19/17.
 */
data class Repository(val owner:User, val name:String, val description:String,
                      val cloneUrl:String, val watchers:Int, val forkCount:Int)