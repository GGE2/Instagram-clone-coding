package com.example.ggestagram.navigation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ggestagram.LoginActivity
import com.example.ggestagram.MainActivity
import com.example.ggestagram.R
import com.example.ggestagram.navigation.model.ContentDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_user.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var fragmentView:View? = null
    var firestore:FirebaseFirestore? = null
    var uid:String? = null
    var auth: FirebaseAuth? = null
    var currentUserUid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = LayoutInflater.from(activity).inflate(R.layout.fragment_user,container,false)
        uid = arguments?.getString("destinationUid")
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        currentUserUid = auth?.uid


        //자신의 정보일 경우
        if(uid ==currentUserUid){
            fragmentView?.account_btn_follow_signout?.text = getString(R.string.signout)
            fragmentView?.account_btn_follow_signout?.setOnClickListener {
                activity?.finish()
                var intent = Intent(activity,LoginActivity::class.java)
                startActivity(intent)
                auth?.signOut()
            }

        }
        //다른 사람의 정보일 경우
        else{
            fragmentView?.account_tv_following?.text = getString(R.string.follow)
            var mainactivity = activity as MainActivity
            mainactivity?.toolbar_tv_userid?.text = arguments?.getString("userId")
            mainactivity?.toolbar_btn_back?.setOnClickListener {
                mainactivity.bottom_navigation.selectedItemId = R.id.action_home
            }

            mainactivity?.toolbar_title_image?.visibility = View.GONE
            mainactivity?.toolbar_tv_userid?.visibility = View.VISIBLE
            mainactivity?.toolbar_btn_back?.visibility = View.VISIBLE



        }


        fragmentView?.account_recylerview?.adapter = UserFragmentRecylerView()
        fragmentView?.account_recylerview?.layoutManager = GridLayoutManager(activity,3)


        return fragmentView
    }

    inner class UserFragmentRecylerView : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        var contentDTOs : ArrayList<ContentDTO> = arrayListOf()

        init{
            firestore?.collection("images")?.whereEqualTo("uid",uid)?.addSnapshotListener { value, error ->

                if(value == null) {
                        return@addSnapshotListener
                }
                else{
                    for(snapshot in value.documents){
                        var data = snapshot.toObject(ContentDTO::class.java)
                        contentDTOs.add(data!!)
                    }
                    fragmentView?.account_tv_post_count?.text = contentDTOs.size.toString()

                    notifyDataSetChanged()
                }

            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

            //화면 폭의 1/3
            var width = resources.displayMetrics.widthPixels/3
            var imageView = ImageView(parent.context)

            imageView.layoutParams = LinearLayoutCompat.LayoutParams(width,width)

            return CustomViewHolder(imageView)

        }

        inner class CustomViewHolder(var imageView: ImageView) : RecyclerView.ViewHolder(imageView) {

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var imageView = (holder as CustomViewHolder).imageView
            Glide.with(holder.itemView.context).load(contentDTOs[position].imageUrl).apply(RequestOptions().centerCrop()).into(imageView)

        }

        override fun getItemCount(): Int {

            return contentDTOs.size

        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                 }
            }
    }

}