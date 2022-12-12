%% T1, T2, T3 sono i poli
%% da i poli e mu possiamo ottenere L (fattorizzazione mu/(...))

%% bode per disegnare diagramma di L

S = tf('s');

%% margin(L) restituisce Ma, Mf, Wpi, Wc
%% non restituisce in decibel, convertire facendo *20*log10()
figura; margin(LL); grid on; 

%% zpk toglie i poli mutlipli (semplificazioni)
%% minreal 
%% pole

%% F con zpk, o connect o feedback
%% sensitività FF:
FF = tf(minreal(F));