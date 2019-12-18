function [theta,stats,Q] = EM(theta,data,Estep,Mstep,feedback,parameters)
% EM: Expectation-Maximization
% [theta,stats,Q] = EM(theta,data,Estep,Mstep,feedback,parameters)
%   theta      - initial guess
%   data       - the 'incomplete' data
%   Estep      - function stats = Estep(theta,data,parameters)
%   Mstep      - function [theta,Q] = Mstep(theta,data,stats,parameters)
%   feedback   - function feedback(i,data,stats,theta,Q)
%   parameters - maxIterations, atol, rtol, Eparameters, Mparameters
% returns:
%   theta      - theta estimate at convergence
%   stats      - sufficient statistics of last iteration

maxIterations=mygetfield(parameters,'maxIterations',100);
atol=mygetfield(parameters,'atol',1e-5);
rtol=mygetfield(parameters,'rtol',1e-5);
Eparameters=mygetfield(parameters,'Eparameters',[]);
Mparameters=mygetfield(parameters,'Mparameters',[]);

% init iteration counter
i=1;

% iterate EM
while 1
   
   % E-step
   % P(hidden|theta,data)
   % as captured by sufficient statistics 'stats'
   stats = feval(Estep,theta,data,Eparameters);
   
   % M-step
   % theta = argmax \int P(theta|hidden,data) P(hidden|theta,data)
   [theta,Q] = feval(Mstep,theta,data,stats,Mparameters);
   
   % feedback
   feval(feedback,i,data,stats,theta,Q);
   
   % test convergence
   if i==maxIterations, break; end
   if i>1 & (Q-lastQ)<=max(rtol*lastQ,atol), break;end
   
   % increase iteration counter and remember value of lastQ
   i=i+1;
   lastQ=Q;
   
end
