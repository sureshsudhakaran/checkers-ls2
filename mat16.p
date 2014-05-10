isempty(B,W,[X,Y]):- not(member([X,Y],B)),not(member([X,Y],W)).

rmv([[P,Q]],[[R,S]|W],H,H1):- [P,Q]=[R,S],rmb(W,[],W2),append(W2,[[]],H2),append(H,H2,H1).
rmv([[P,Q]],[[R,S]|W],H,H1):- append([[R,S]],H,H2),rmv([[P,Q]],W,H2,H1).
rmv(_,[],H1,H1).

rmb([[]|W],H,H1):- append(W,H,H1).
rmb([[P,Q]|W],H,H1):- append([[P,Q]],H,H2), rmb(W,H2,H1).


capgen([[X,Y]|B],W,N,[R,S],[P,Q]):- capposmove(N,W,[[X,Y]|B],[P,Q]),R=X,S=Y.
capgen([[X,Y]|B],W,N,[R,S],[P,Q]):- append(B,[[X,Y]],C),capgen(C,W,N,[R,S],[P,Q]).
capgen([[]|_],_,_,[0,0],[0,0]).
capposmove(N,W,[[X,Y]|B],[R,S]):- P is X+1, Q is Y+1, member([P,Q],W), R is X+2, S is Y+2, R=<N , S=<N, isempty(B,W,[R,S]).
capposmove(N,W,[[X,Y]|B],[R,S]):- P is X+1, Q is Y-1, member([P,Q],W), R is X+2, S is Y-2, R=<N, S>0, isempty(B,W,[R,S]).
caprearr(B,W,H2,D2,-1):- capgen(B,W,8,H2,D2), H2=[_,_], D2=[P,_], P is 8.
caprearr(B,W,H2,D2,K):- capgen(B,W,8,H2,D2), H2=[X,Y], rmv([[X,Y]],B,[],H1), append(H1,[[]],H3), append([D2],H3,H4), vulnerab(8,W,H4,0,K).

gen([[X,Y]|B],W,N,[R,S],[P,Q]):- posmove(N,W,[[X,Y]|B],[P,Q]),R=X,S=Y.
gen([[X,Y]|B],W,N,[R,S],[P,Q]):- append(B,[[X,Y]],C),gen(C,W,N,[R,S],[P,Q]).
gen([[]|_],_,_,[0,0],[0,0]).

posmove(N,W,[[X,Y]|B],[P,Q]):-capposmove(N,W,[[X,Y]|B],[P,Q]),!.
posmove(N,W,[[X,Y]|B],[P,Q]):- P is X+1, Q is Y+1, P =<N, Q=<N, isempty(B,W,[P,Q]).
posmove(N,W,[[X,Y]|B],[P,Q]):- P is X+1, Q is Y-1, P =<N, Q>0,  isempty(B,W,[P,Q]).

vulnerab(_,_,[[]|_],L,L).
vulnerab(N,W,[[X,Y]|B],L,V):- P is X+1, Q is Y+1, member([P,Q],W), R is X-1, S is Y-1, R > 0, S > 0, isempty(B,W,[R,S]),append(B,[[X,Y]],C), L2 is L+1,!,vulnerab(N,W,C,L2,V).
vulnerab(N,W,[[X,Y]|B],L,V):- P is X+1, Q is Y-1, member([P,Q],W), R is X-1, S is Y+1, R > 0, S =< N, isempty(B,W,[R,S]),append(B,[[X,Y]],C),L2 is L+1,!,vulnerab(N,W,C,L2,V).
vulnerab(8,W,[[X,Y]|B],L,V):- append(B,[[X,Y]],C),!,vulnerab(8,W,C,L,V).


rearr(B,W,H2,D2,-1):- gen(B,W,8,H2,D2), H2=[_,_], D2=[P,_], P is 8.
rearr(B,W,H2,D2,K):- gen(B,W,8,H2,D2), H2=[X,Y], rmv([[X,Y]],B,[],H1), append(H1,[[]],H3), append([D2],H3,H4), vulnerab(8,W,H4,0,K).


board(B,W,H2,D2):- caprearr(B,W,H2,D2,-1).
board(B,W,H2,D2):- caprearr(B,W,H2,D2,0).
board(B,W,H2,D2):- caprearr(B,W,H2,D2,1).
board(B,W,H2,D2):- caprearr(B,W,H2,D2,2).
board(B,W,H2,D2):- caprearr(B,W,H2,D2,3).
board(B,W,H2,D2):- caprearr(B,W,H2,D2,4).
board(B,W,H2,D2):- caprearr(B,W,H2,D2,5).
board(B,W,H2,D2):- caprearr(B,W,H2,D2,6).
board(B,W,H2,D2):- caprearr(B,W,H2,D2,7).
board(B,W,H2,D2):- caprearr(B,W,H2,D2,8).
board(B,W,H2,D2):- rearr(B,W,H2,D2,-1).
board(B,W,H2,D2):- rearr(B,W,H2,D2,0).
board(B,W,H2,D2):- rearr(B,W,H2,D2,1).
board(B,W,H2,D2):- rearr(B,W,H2,D2,2).
board(B,W,H2,D2):- rearr(B,W,H2,D2,3).
board(B,W,H2,D2):- rearr(B,W,H2,D2,4).
board(B,W,H2,D2):- rearr(B,W,H2,D2,5).
board(B,W,H2,D2):- rearr(B,W,H2,D2,6).
board(B,W,H2,D2):- rearr(B,W,H2,D2,7).
board(B,W,H2,D2):- rearr(B,W,H2,D2,8).

read_file(Stream,[]) :-
    at_end_of_stream(Stream).

read_file(Stream,[X|L]) :-
    \+ at_end_of_stream(Stream),
    read(Stream,X),
    read_file(Stream,L).


checker:-
open("un.txt",read,Te),
\+ at_end_of_stream(Te),
read(Te,[B,W]),
board(B,W,H2,D2),
append([H2],[D2],Hy),
write(Hy),
close(Te).



