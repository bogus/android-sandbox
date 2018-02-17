package net.bogus.githubsearch.model

/**
 * Created by burak on 11/19/17.
 */
data class Repository(val owner:User, val name:String, val description:String,
                      val url:String, val watchers:Int, val forkCount:Int) {

    override fun equals(other: Any?): Boolean {
        val otherRepository = other as? Repository
        otherRepository?.let {
            return this.url.equals(it.url)
        }
        return false
    }
}