using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using static ServerExemplu.User;

namespace ServerExemplu
{
    class ClientHandler
    {
        private Socket _sk = null;
        private Thread _th = null;
        private bool _shouldRun = true;
        private bool _isRunning = true;
        public ClientHandler(Socket sk)
        {
            _sk = sk;
        }

        public void initClient()
        {
            if (null != _th)
                return;

            _th = new Thread(new ThreadStart(run));
            _th.Start();
        }

        public void stopClient()
        {
            if (_th == null )
                return;

            _sk.Close();
            _shouldRun = false;
        }

        public bool SocketConnected(Socket s)
        {
            bool part1 = s.Poll(10000, SelectMode.SelectRead);
            bool part2 = (s.Available == 0);
            if (part1 && part2)
                return false;
            else
                return true;
        }
        private void handleMsg(String msg)
        {
                                 
            //prelucrare msg.....
            
            String[] split=msg.Split(' ');

            byte[] bytesMsgRaspuns = Encoding.ASCII.GetBytes(msg);

            Console.WriteLine("Client " + ": " + msg);
            _sk.Send(bytesMsgRaspuns);


            UtilizatorDbContext db = new UtilizatorDbContext();
            
                var user = db.Utilizatori.SingleOrDefault(o => o.Nume == split[2]);
                if (user != null) 
                {
                    user.Latitudine = split[0];
                    user.Longitudine = split[1];
                db.SaveChanges();
            }
                else
                {
                    User newUser = new User();
                    newUser.Nume = split[2];
                    newUser.Latitudine = split[0];
                    newUser.Longitudine = split[1];
                    db.Utilizatori.Add(newUser);
                }
                
                
            
            

            
            
        }
        

        
        private void run()
        {
            // Attention! This is the largest message one can receive!
            

            while (_shouldRun)
            {
                //Console.WriteLine("Client... "+_idx);
                byte[] rawMsg = new byte[50];
                try
                {
                    
                        int bCount = _sk.Receive(rawMsg);
                        String msg = Encoding.UTF8.GetString(rawMsg);
                        if (bCount > 0)
                     
                        handleMsg(msg);
                                     
                        
                }
                catch (Exception ex)
                {
                    //Console.WriteLine("Client exxxccp ");
                    return;
                }
                Thread.Sleep(1);
            }
            _isRunning = false;
            
        }

        public bool isAlive()
        {
            return _isRunning;
        }
        public void deleteUser()
        {
            UtilizatorDbContext db = new UtilizatorDbContext();
            foreach (var item in db.Utilizatori)
            {
                db.Utilizatori.Remove(item);
            }
            db.SaveChanges();
        }
    }
}
