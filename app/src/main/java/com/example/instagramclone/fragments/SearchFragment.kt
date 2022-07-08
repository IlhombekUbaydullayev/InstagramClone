package com.example.instagramclone.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.adapter.SearchAdapter
import com.example.instagramclone.managers.AuthManager
import com.example.instagramclone.managers.DatabaseManager
import com.example.instagramclone.managers.handler.DBFollowHandler
import com.example.instagramclone.managers.handler.DBUserHandler
import com.example.instagramclone.managers.handler.DBUsersHandler
import com.example.instagramclone.model.User
import com.example.instagramclone.utils.Utils
import com.example.instagramclone.R

/**
   * In SearchFragment, all registered users can be found by searching keyword and followed.
   * **/
class SearchFragment : BaseFragment() {
    val TAG = SearchFragment::class.java.simpleName
    lateinit var rv_home: RecyclerView
    var items = ArrayList<User>()
    var users = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        rv_home = view.findViewById(R.id.rv_home)
        rv_home.setLayoutManager(GridLayoutManager(activity, 1))
        val et_search = view.findViewById<EditText>(R.id.et_search)
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val keyword = s.toString().trim()
                usersByKeyword(keyword)
            }
        })
        loadUsers()
    }

    private fun refreshAdapter(items: ArrayList<User>) {
        val adapter = SearchAdapter(this, items)
        rv_home.adapter = adapter
    }

    fun usersByKeyword(keyword: String) {
        if (keyword.isEmpty())
            refreshAdapter(items)
        users.clear()
        for (user in items)
            if (user.fullname.toLowerCase().startsWith(keyword.toLowerCase()))
                users.add(user)

        refreshAdapter(users)
    }

    private fun loadUsers() {
        val uid = AuthManager.currentUser()!!.uid
        DatabaseManager.loadUsers(object : DBUsersHandler {
            override fun onSuccess(users: ArrayList<User>) {
                DatabaseManager.loadFollowing(uid, object : DBUsersHandler {
                    override fun onSuccess(following: ArrayList<User>) {
                        items.clear()
                        items.addAll(mergedUsers(uid,users, following))
                        refreshAdapter(items)
                    }

                    override fun onError(e: Exception) {

                    }
                })
            }

            override fun onError(e: Exception) {

            }
        })
    }

    private fun mergedUsers(uid:String,
                            users: ArrayList<User>,
                            following: ArrayList<User>): ArrayList<User> {
        val items = ArrayList<User>()
        for (u in users){
            val user = u
            for(f in following){
                if(u.uid == f.uid){
                    user.isFollowed = true
                    break
                }
            }
            if(uid != user.uid){
                items.add(user)
            }
        }
        return items
    }

    fun followOrUnfollow(to: User) {
        val uid = AuthManager.currentUser()!!.uid
        if (!to.isFollowed) {
            followUser(uid, to)
        } else {
            unFollowUser(uid, to)
        }
    }

    private fun followUser(uid: String, to: User) {
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DatabaseManager.followUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isFollowed: Boolean) {
                        to.isFollowed = isFollowed
                        DatabaseManager.storePostsToMyFeed(uid, to)
                        Utils.sendNote(requireContext(), me, to.device_token)
                    }

                    override fun onError(e: Exception) {
                    }
                })
            }
            override fun onError(e: Exception) {

            }
        })
    }

    private fun unFollowUser(uid: String, to: User) {
        DatabaseManager.loadUser(uid, object : DBUserHandler {
            override fun onSuccess(me: User?) {
                DatabaseManager.unFollowUser(me!!, to, object : DBFollowHandler {
                    override fun onSuccess(isFollowed: Boolean) {
                        to.isFollowed = isFollowed
                        DatabaseManager.removePostsFromMyFeed(uid, to)
                    }

                    override fun onError(e: Exception) {

                    }
                })
            }

            override fun onError(e: Exception) {

            }
        })
    }
}